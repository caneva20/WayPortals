package me.caneva20.wayportals.portal;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.CustomLog;
import lombok.val;
import me.caneva20.messagedispatcher.dispachers.IMessageDispatcher;
import me.caneva20.wayportals.portal.config.IPortalConfig;
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
import me.caneva20.wayportals.utils.events.FakeBlockBreakEvent;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
@CustomLog
public class PortalManager implements IPortalManager {

  private final IPortalDatabase db;
  private final PluginManager pluginManager;
  private final IPortalConfig config;
  private final IMessageDispatcher dispatcher;

  @Inject
  PortalManager(IPortalDatabase db, PluginManager pluginManager, IPortalConfig config,
      IMessageDispatcher dispatcher) {
    this.db = db;
    this.pluginManager = pluginManager;
    this.config = config;
    this.dispatcher = dispatcher;
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

  public @Nullable Portal create(Location location, Player player) {
    var region = PortalUtility.find(location.getBlock());

    if (!canBuild(player, location, region)) {
      //TODO: Load string from lang file
      dispatcher.error(player, "Sorry, but you can't build here");

      return null;
    }

    if (region == null) {
      return null;
    }

    val createEvent = new PortalCreateEvent(region);
    pluginManager.callEvent(createEvent);

    if (createEvent.isCancelled()) {
      return null;
    }

    var record = db.create(region);

    if (record == null) {
      return null;
    }

    return build(record);
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

  private boolean canBuild(Player player, Location location, Region region) {
    Set<Block> blocks = new HashSet<>();

    var world = location.getWorld();

    switch (config.buildPermissionCheckMode()) {
      case CORNERS:
        val minX = region.from().x();
        val maxX = region.to().x();

        val minY = region.from().y();
        val maxY = region.to().y();

        val minZ = region.from().z();
        val maxZ = region.to().z();

        blocks.add(new Location(world, minX, minY, minZ).getBlock());
        blocks.add(new Location(world, minX, maxY, minZ).getBlock());

        blocks.add(new Location(world, maxX, minY, maxZ).getBlock());
        blocks.add(new Location(world, maxX, maxY, maxZ).getBlock());
        break;
      case ALL:
        for (int x = region.from().x(); x <= region.to().x(); x++) {
          for (int y = region.from().y(); y <= region.to().y(); y++) {
            for (int z = region.from().z(); z <= region.to().z(); z++) {
              blocks.add(new Location(world, x, y, z).getBlock());
            }
          }
        }
        break;
      case FIRST:
        blocks.add(location.getBlock());
        break;
    }

    for (Block block : blocks) {
      var fakeEvent = new FakeBlockBreakEvent(block, player);

      pluginManager.callEvent(fakeEvent);

      if (fakeEvent.isCancelled()) {
        return false;
      }
    }

    return true;
  }
}