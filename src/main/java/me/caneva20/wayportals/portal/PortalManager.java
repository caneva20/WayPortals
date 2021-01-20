package me.caneva20.wayportals.portal;

import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.caneva20.wayportals.portal.db.IPortalDatabase;
import me.caneva20.wayportals.portal.db.PortalRecord;
import me.caneva20.wayportals.utils.Vector2;
import me.caneva20.wayportals.utils.Vector3;
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
      record = db.create(region);
    }

    if (record == null) {
      //Portal could not be created

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

    link(portal.link, null);
    link(portal, null);

    db.delete(portal.id());
  }

  @Override
  public void link(@NotNull Portal portal, @Nullable Portal target) {
    if (target == null) {
      db.setLinkId(portal.id(), null);

      portal.link = null;
      return;
    }

    if (portal.link != null && (portal.link == portal || portal.link.id() == portal.id())) {
      return;
    }

    if (portal.link != null) {
      link(portal.link, null);
    }

    portal.link = portal;
    db.setLinkId(portal.id(), target.id());

    link(target, portal);
  }

  private PortalOrientation getOrientation(PortalRecord record) {
    var pos = new Vector3(record.minX(), record.minY(), record.minZ());

    return record.minZ() != record.maxZ() ? PortalOrientation.northSouth(pos)
        : PortalOrientation.westEast(pos);
  }

  private Portal create(@NotNull PortalRecord record) {
    val orientation = getOrientation(record);

    val min = new WorldVector3(record.minX(), record.minY(), record.minZ(), record.world());
    val max = new WorldVector3(record.maxX(), record.maxY(), record.maxZ(), record.world());

    var portal = new Portal(record.id(), min, max, orientation);

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