package me.caneva20.wayportals.portal;

import co.aikar.idb.DB;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

  public Portal(Vector3Int min, Vector3Int max, @Nullable String worldName) {
    super(min, max, worldName);

    if (exists()) {
      load();
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

  private void load() {
    try {
      var row = DB.getFirstRow(
          "SELECT id, linked_portal_id FROM portals WHERE world = ? AND min_x = ? AND min_y = ? AND min_z = ? AND max_x = ? AND max_y = ? AND max_z = ? LIMIT 1",
          worldName(), from().x(), from().y(), from().z(), to().x(), to().y(), to().z());

      this.id = row.getInt("id");

      Pool.add(this);

      var linkedPortalId = (Number) row.get("linked_portal_id");

      if (linkedPortalId != null) {
        link = Pool.find(linkedPortalId.longValue());

        if (link == null) {
          link = find(linkedPortalId.longValue());
        }

        if (link != null) {
          link.link(this);
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

      Pool.add(this);
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

  public void link(@Nullable Portal portal) {
    if (portal == null) {
      setLinkId(null);

      link = null;
      return;
    }

    if (link != null && (link == portal || link.id() == portal.id())) {
      return;
    }

    if (hasLink()) {
      link.link(null);
    }

    link = portal;
    setLinkId(portal.id());

    portal.link(this);
  }

  public boolean hasLink() {
    return link != null;
  }

  public void delete() {
    Pool.remove(this);

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