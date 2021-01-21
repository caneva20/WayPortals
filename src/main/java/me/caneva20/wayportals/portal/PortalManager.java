package me.caneva20.wayportals.portal;

import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.caneva20.wayportals.portal.db.IPortalDatabase;
import me.caneva20.wayportals.portal.db.PortalRecord;
import me.caneva20.wayportals.portal.events.PortalCreateEvent;
import me.caneva20.wayportals.portal.events.PortalDeleteEvent;
import me.caneva20.wayportals.portal.events.PortalLinkEvent;
import me.caneva20.wayportals.portal.events.PortalLinkedEvent;
import me.caneva20.wayportals.portal.events.PortalUnlinkEvent;
import me.caneva20.wayportals.utils.WorldVector3;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class PortalManager implements IPortalManager {

  private final IPortalDatabase db;
  private final PluginManager pluginManager;

  /**
   * Creates or load a portal at [location]
   *
   * @return The portal found at [location] or null if none is found
   */
  @Override
  public @Nullable Portal get(Location location) {
    var region = PortalUtility.find(location.getBlock());

    if (region == null) {
      return null;
    }

    var record = db.find(region);

    if (record == null) {
      val event = new PortalCreateEvent(region);
      pluginManager.callEvent(event);

      if (event.isCancelled()) {
        return null;
      }

      record = db.create(region);
    }

    if (record == null) {
      return null;
    }

    return create(record);
  }

  @Override
  public @Nullable Portal find(int id) {
    final var record = db.find(id);

    if (record == null) {
      return null;
    }

    return create(record);
  }

  @Override
  public void delete(@NotNull Portal portal) {
    Pool.remove(portal);

    if (portal.link != null) {
      link(portal.link, null);
    }

    link(portal, null);

    db.delete(portal.id());

    pluginManager.callEvent(new PortalDeleteEvent(portal));
  }

  @Override
  public void link(@NotNull Portal src, @Nullable Portal dst) {
    if (dst == null) {
      db.setLinkId(src.id(), null);

      src.link = null;
      return;
    }

    if (src.link != null && (src.link == dst || src.link.id() == dst.id())) {
      return;
    }

    val event = new PortalLinkEvent(src, dst);
    pluginManager.callEvent(event);

    if (event.isCancelled()) {
      return;
    }

    if (src.link != null) {
      link(src.link, null);

      pluginManager.callEvent(new PortalUnlinkEvent(src, src.link));
    }

    src.link = dst;
    db.setLinkId(src.id(), dst.id());

    link(dst, src);

    pluginManager.callEvent(new PortalLinkedEvent(src, dst));
  }

  private Portal create(@NotNull PortalRecord record) {
    val min = new WorldVector3(record.minX(), record.minY(), record.minZ(), record.world());
    val max = new WorldVector3(record.maxX(), record.maxY(), record.maxZ(), record.world());

    var portal = new Portal(record.id(), min, max);

    Pool.add(portal);

    if (record.linkedPortalId() != null) {
      //Load link
      @SuppressWarnings({"UnnecessaryUnboxing", "ConstantConditions"}) val linkedPortalId = record
          .linkedPortalId().intValue();

      portal.link = Pool.find(linkedPortalId);

      if (portal.link == null) {
        //Load linked portal
        var linkRecord = db.find(linkedPortalId);

        if (linkRecord != null) {
          portal.link = create(linkRecord);
        }
      }
    }

    return portal;
  }
}

class Pool {

  private static final Set<Portal> pool = new HashSet<>();

  static void add(Portal portal) {
    pool.add(portal);
  }

  static void remove(Portal portal) {
    pool.remove(portal);
  }

  static @Nullable Portal find(long id) {
    return pool.stream().filter(x -> x.id() == id).findFirst().orElse(null);
  }
}