package me.caneva20.wayportals.portal;

import co.aikar.idb.DB;
import java.sql.SQLException;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import me.caneva20.wayportals.utils.Region;
import me.caneva20.wayportals.utils.Vector2;
import me.caneva20.wayportals.utils.Vector3Int;
import org.jetbrains.annotations.Nullable;

@ToString
public class Portal extends Region {

  @Getter
  private long id;

  @Getter
  private PortalOrientation orientation;

  @Getter
  private final PortalDimensions dimensions;

  @Getter
  @ToString.Exclude
  private Portal link;

  private Portal(Vector3Int min, Vector3Int max, @Nullable String worldName, boolean loadLInk) {
    super(min, max, worldName);

    if (exists()) {
      load(loadLInk);
    } else {
      create();
    }

    updateOrientation();

    if (orientation.axis() == OrientationAxis.X) {
      dimensions = new PortalDimensions(new Vector2(min.x(), min.y()),
          new Vector2(max.x(), max.y()));
    } else {
      dimensions = new PortalDimensions(new Vector2(min.z(), min.y()),
          new Vector2(max.z(), max.y()));
    }
  }

  public Portal(Vector3Int min, Vector3Int max, @Nullable String worldName) {
    this(min, max, worldName, true);
  }

  private void updateOrientation() {
    orientation = from().z() != to().z() ? PortalOrientation.northSouth(from().toVector3())
        : PortalOrientation.westEast(from().toVector3());
  }

  private boolean exists() {
    try {
      int count = DB.getFirstColumn(
          "SELECT count() FROM portals WHERE world = ? AND min_x = ? AND min_y = ? AND min_z = ? AND max_x = ? AND max_y = ? AND max_z = ? LIMIT 1",
          worldName(), from().x(), from().y(), from().z(), to().x(), to().y(), to().z());

      return count > 0;
    } catch (SQLException ex) {
      ex.printStackTrace();

      return false;
    }
  }

  private void load(boolean loadLink) {
    try {
      var row = DB.getFirstRow(
          "SELECT id, linked_portal_id FROM portals WHERE world = ? AND min_x = ? AND min_y = ? AND min_z = ? AND max_x = ? AND max_y = ? AND max_z = ? LIMIT 1",
          worldName(), from().x(), from().y(), from().z(), to().x(), to().y(), to().z());

      this.id = row.getInt("id");

      if (loadLink) {
        var linkedPortalId = (Number) row.get("linked_portal_id");

        if (linkedPortalId != null) {
          link = find(linkedPortalId.intValue(), false);
        }
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  private void create() {
    try {
      this.id = DB.executeInsert(
          "INSERT INTO portals(world, min_x, min_y, min_z, max_x, max_y, max_z) VALUES(?, ?, ?, ?, ?, ?, ?)",
          worldName(), from().x(), from().y(), from().z(), to().x(), to().y(), to().z());
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    deleteOverlapping();
  }

  private void deleteOverlapping() {
    try {
      List<Integer> results = DB.getFirstColumnResults(
          "SELECT id from portals WHERE ? = world AND ? <= max_x AND ? >= min_x AND ? <= max_y AND ? >= min_y AND ? <= max_z AND ? >= min_z AND id != ?",
          worldName(), from().x(), to().x(), from().y(), to().y(), from().z(), to().z(), id());

      for (Integer id : results) {
        DB.executeUpdate("DELETE FROM portals WHERE id = ?", id);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  private void setLinkId(@Nullable Long linkedPortalId) {
    try {
      DB.executeUpdate("UPDATE portals SET linked_portal_id = ? WHERE id = ?", linkedPortalId, id);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  private void link(@Nullable Portal portal, boolean updateOther) {
    if (hasLink()) {
      link.link(null, false);
    }

    if (portal == null) {
      setLinkId(null);
    } else {
      setLinkId(portal.id());
      link = portal;

      if (updateOther) {
        portal.link(this, false);
      }
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
          worldName(), from().x(), from().y(), from().z(), to().x(), to().y(), to().z(), id);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public PortalDimensions dimensions(boolean inverse) {
    if (inverse) {
      return dimensions().inverse();
    } else {
      return dimensions();
    }
  }

  public boolean hasLink() {
    return link != null;
  }

  public void link(@Nullable Portal portal) {
    link(portal, true);
  }

  private static @Nullable Portal find(long id, boolean loadLink) {
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

      return new Portal(new Vector3Int(minX, minY, minZ), new Vector3Int(maxX, maxY, maxZ), world,
          loadLink);
    } catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public static @Nullable Portal find(long id) {
    return find(id, true);
  }
}
