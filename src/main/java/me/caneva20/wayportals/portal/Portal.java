package me.caneva20.wayportals.portal;

import co.aikar.idb.DB;
import java.sql.SQLException;
import lombok.Getter;
import lombok.ToString;
import me.caneva20.wayportals.utils.Region;
import me.caneva20.wayportals.utils.Vector3Int;
import org.jetbrains.annotations.Nullable;

@ToString
public class Portal extends Region {

  @Getter
  private long id;

  public Portal(Vector3Int min, Vector3Int max, @Nullable String worldName) {
    super(min, max, worldName);

    if (exists()) {
      load();
    } else {
      create();
    }
  }

  private boolean exists() {
    try {
      int count = DB.getFirstColumn(
          "SELECT count() FROM portals WHERE world = ? AND min_x = ? AND min_y = ? AND min_z = ? AND max_x = ? AND max_y = ? AND max_z = ? LIMIT 1",
          getWorldName(), getFrom().getX(), getFrom().getY(), getFrom().getZ(), getTo().getX(),
          getTo().getY(), getTo().getZ());

      return count > 0;
    } catch (SQLException ex) {
      ex.printStackTrace();

      return false;
    }
  }

  private void load() {
    try {
      //The (int) cast IS required
      //noinspection RedundantCast
      this.id = (int)DB.getFirstColumn(
          "SELECT id FROM portals WHERE world = ? AND min_x = ? AND min_y = ? AND min_z = ? AND max_x = ? AND max_y = ? AND max_z = ? LIMIT 1",
          getWorldName(), getFrom().getX(), getFrom().getY(), getFrom().getZ(), getTo().getX(),
          getTo().getY(), getTo().getZ());
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  private void create() {
    try {
      this.id = DB.executeInsert(
          "INSERT INTO portals(world, min_x, min_y, min_z, max_x, max_y, max_z) VALUES(?, ?, ?, ?, ?, ?, ?)",
          getWorldName(), getFrom().getX(), getFrom().getY(), getFrom().getZ(), getTo().getX(),
          getTo().getY(), getTo().getZ());
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public void delete() {
    try {
      DB.executeUpdate("DELETE FROM portals WHERE id = ?", id);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public void save() {
    try {
      DB.executeUpdate(
          "UPDATE portals SET world = ?, min_x = ?, min_y = ?, min_z = ?, max_x = ?, max_y = ?, max_z = ? WHERE id = ?",
          getWorldName(), getFrom().getX(), getFrom().getY(), getFrom().getZ(), getTo().getX(),
          getTo().getY(), getTo().getZ(), id);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public static @Nullable Portal find(long id) {
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

      return new Portal(new Vector3Int(minX, minY, minZ), new Vector3Int(maxX, maxY, maxZ), world);
    } catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }
  }
}
