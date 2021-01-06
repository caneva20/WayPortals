package me.caneva20.wayportals.portal;

import static co.aikar.idb.DB.executeUpdate;

import co.aikar.idb.DB;
import java.sql.SQLException;
import javax.inject.Inject;
import javax.inject.Singleton;
import me.caneva20.messagedispatcher.dispachers.IConsoleMessageDispatcher;
import me.caneva20.wayportals.utils.Vector3Int;
import org.jetbrains.annotations.Nullable;

@Singleton
public class PortalDB {

  private final IConsoleMessageDispatcher dispatcher;

  @Inject
  PortalDB(IConsoleMessageDispatcher dispatcher) {
    this.dispatcher = dispatcher;
  }

  public boolean exists(Portal portal) {
    try {
      var min = portal.getFrom();
      var max = portal.getTo();

      int count = DB.getFirstColumn(
          "SELECT count() FROM portals WHERE world = ? AND min_x = ? AND min_y = ? AND min_z = ? AND max_x = ? AND max_y = ? AND max_z = ? LIMIT 1",
          portal.getWorldName(), min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(),
          max.getZ());

      return count > 0;
    } catch (SQLException ex) {
      ex.printStackTrace();

      return false;
    }
  }

  public void create(Portal portal) {
    if (exists(portal)) {
      dispatcher.info("Portal " + portal.id() + " already registered");

      return;
    }

    var min = portal.getFrom();
    var max = portal.getTo();

    try {
      var id = DB.executeInsert(
          "INSERT INTO portals(world, min_x, min_y, min_z, max_x, max_y, max_z) VALUES(?, ?, ?, ?, ?, ?, ?)",
          portal.getWorldName(), min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(),
          max.getZ());

      portal.id(id);

    } catch (SQLException ex) {
      dispatcher.error("[PortalsDB] Portal creation failed");

      ex.printStackTrace();
    }
  }

  public void load(Portal portal) {
    if (exists(portal)) {
      var min = portal.getFrom();
      var max = portal.getTo();

      try {
        int id = DB.getFirstColumn(
            "SELECT id FROM portals WHERE world = ? AND min_x = ? AND min_y = ? AND min_z = ? AND max_x = ? AND max_y = ? AND max_z = ? LIMIT 1",
            portal.getWorldName(), min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(),
            max.getZ());

        portal.id(id);
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
  }

  public @Nullable Portal find(long id) {
    try {
      var row = DB.getFirstRow("SELECT * FROM portals WHERE id = ? LIMIT 1", id);

      if (row == null) {
        return null;
      }

      var world = row.getString("world");
      var minX = row.getInt("min_x");
      var minY = row.getInt("min_y");
      var minZ = row.getInt("min_z");
      var maxX = row.getInt("max_x");
      var maxY = row.getInt("max_y");
      var maxZ = row.getInt("max_z");

      return new Portal(id, new Vector3Int(minX, minY, minZ), new Vector3Int(maxX, maxY, maxZ),
          world);
    } catch (SQLException ex) {
      ex.printStackTrace();

      dispatcher.error(String.format("An error occurred while search for portal %s", id));

      return null;
    }
  }

  public void delete(Portal portal) {
    try {
      executeUpdate("DELETE FROM portals WHERE id = ?", portal.id());
    } catch (SQLException ex) {
      ex.printStackTrace();

      dispatcher.error(String.format("[PortalsDB] Portal %s failed to be deleted", portal.id()));
    }
  }
}