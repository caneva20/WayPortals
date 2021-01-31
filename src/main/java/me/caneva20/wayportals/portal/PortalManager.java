package me.caneva20.wayportals.portal;

import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.val;
import me.caneva20.wayportals.portal.db.IPortalDatabase;
import me.caneva20.wayportals.portal.db.PortalRecord;
import me.caneva20.wayportals.portal.events.PortalCreateEvent;
import me.caneva20.wayportals.portal.events.PortalDeletedEvent;
import me.caneva20.wayportals.portal.events.PortalLinkEvent;
import me.caneva20.wayportals.portal.events.PortalLinkedEvent;
import me.caneva20.wayportals.portal.events.PortalUnlinkedEvent;
import me.caneva20.wayportals.portal.events.PortalUpdateEvent;
import me.caneva20.wayportals.portal.events.PortalUpdatedEvent;
import me.caneva20.wayportals.utils.Region;
import me.caneva20.wayportals.utils.WorldVector3;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class PortalManager implements IPortalManager {

  private final IPortalDatabase db;
  private final PluginManager pluginManager;

  @Inject
  PortalManager(IPortalDatabase db, PluginManager pluginManager) {
    this.db = db;
    this.pluginManager = pluginManager;
  }

  @Override
  @Nullable
  public Portal get(Location location) {
    var region = PortalUtility.find(location.getBlock());

    if (region == null) {
      return null;
    }

    var record = db.find(region);

    if (record == null) {
      record = create(region);
    }

    if (record == null) {
      return null;
    }

    return build(record);
  }

  @Override
  @Nullable
  public Portal find(@Nullable Long id) {
    if (id == null) {
      return null;
    }

    final var record = db.find(id);

    if (record == null) {
      return null;
    }

    return build(record);
  }

  @Override
  public @Nullable Portal findLink(@Nullable Long id) {
    if (id == null) {
      return null;
    }

    var portal = find(id);

    if (portal == null) {
      return null;
    }

    if (portal.linkId == null) {
      return null;
    }

    return find(portal.linkId);
  }

  @Override
  public void update(@NotNull Portal portal) {
    var event = new PortalUpdateEvent(portal.id());

    pluginManager.callEvent(event);

    if (event.isCancelled()) {
      return;
    }

    db.update(new PortalRecord(portal));

    pluginManager.callEvent(new PortalUpdatedEvent(portal.id()));
  }

  @Override
  public void delete(@NotNull Portal portal) {
    if (portal.linkId != null) {
      val link = find(portal.linkId);

      if (link != null) {
        unlink(link);
      }
    }

    db.delete(portal.id());

    pluginManager.callEvent(new PortalDeletedEvent(portal.id()));
  }

  @Override
  public void link(@NotNull Portal src, @NotNull Portal dst) {
    val event = new PortalLinkEvent(src.id(), dst.id());
    pluginManager.callEvent(event);

    if (event.isCancelled()) {
      return;
    }

    //Remove current src link, if it exists
    if (src.linkId != null) {
      unlink(Objects.requireNonNull(find(src.linkId)));
    }

    //Remove current dst link, if it exists
    if (dst.linkId != null) {
      unlink(Objects.requireNonNull(find(dst.linkId)));
    }

    link(src, dst.id());
    link(dst, src.id());

    pluginManager.callEvent(new PortalLinkedEvent(src.id(), dst.id()));
  }

  private void unlink(@NotNull Portal portal) {
    link(portal, (Long) null);

    pluginManager.callEvent(new PortalUnlinkedEvent(portal.id(), portal.linkId()));
  }

  private void link(@NotNull Portal src, @Nullable Long dstId) {
    src.linkId(dstId);
    db.setLinkId(src.id(), dstId);
  }

  private Portal build(@NotNull PortalRecord record) {
    val min = new WorldVector3(record.minX(), record.minY(), record.minZ(), record.world());
    val max = new WorldVector3(record.maxX(), record.maxY(), record.maxZ(), record.world());

    var portal = new Portal(record.id(), min, max);

    portal.name(record.name());
    portal.linkId(record.linkedPortalId());

    return portal;
  }

  private @Nullable PortalRecord create(Region region) {
    val createEvent = new PortalCreateEvent(region);
    pluginManager.callEvent(createEvent);

    if (createEvent.isCancelled()) {
      return null;
    }

    return db.create(region);
  }
}