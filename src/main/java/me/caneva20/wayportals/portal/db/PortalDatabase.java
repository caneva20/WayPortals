package me.caneva20.wayportals.portal.db;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import me.caneva20.messagedispatcher.dispachers.IConsoleMessageDispatcher;
import me.caneva20.wayportals.utils.Region;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class PortalDatabase implements IPortalDatabase {

  private final IConsoleMessageDispatcher dispatcher;

  @Inject
  PortalDatabase(IConsoleMessageDispatcher dispatcher) {
    this.dispatcher = dispatcher;
  }

  @Override
  public void initialize() {
    try {
      DB.executeUpdate(
          "CREATE TABLE IF NOT EXISTS 'portals' ('id' INTEGER NOT NULL, 'linked_portal_id' INTEGER, 'name' TEXT NOT NULL DEFAULT '', 'world' TEXT NOT NULL DEFAULT '', 'min_x' INTEGER NOT NULL DEFAULT 0, 'min_y' INTEGER NOT NULL DEFAULT 0, 'min_z' INTEGER NOT NULL DEFAULT 0, 'max_x' INTEGER NOT NULL DEFAULT 0, 'max_y' INTEGER NOT NULL DEFAULT 0, 'max_z' INTEGER NOT NULL DEFAULT 0, PRIMARY KEY('id' AUTOINCREMENT), FOREIGN KEY('linked_portal_id') REFERENCES 'portals'('id'))");

      DB.executeUpdate("CREATE INDEX IF NOT EXISTS 'portal_id' ON 'portals'('id')");
    } catch (SQLException ex) {
      logException(ex);
    }
  }

  @Override
  @Nullable
  public PortalRecord find(long id) {
    return createRecord(findRow(id));
  }

  @Override
  @Nullable
  public PortalRecord find(Region region) {
    return createRecord(findRow(region));
  }

  @Override
  @Nullable
  public PortalRecord create(Region region) {
    try {
      var id = DB.executeInsert(
          "INSERT INTO portals(world, min_x, min_y, min_z, max_x, max_y, max_z) VALUES(?, ?, ?, ?, ?, ?, ?)",
          region.worldName(), region.from().x(), region.from().y(), region.from().z(),
          region.to().x(), region.to().y(), region.to().z());

      deleteOverlapping(region, id);

      return new PortalRecord(id, "", region.worldName(), region.from().x(), region.from().y(),
          region.from().z(), region.to().x(), region.to().y(), region.to().z(), null);
    } catch (SQLException ex) {
      logException(ex);
    }

    return null;
  }

  @Override
  public void update(@NotNull PortalRecord record) {
    try {
      DB.executeUpdate(
          "UPDATE portals SET name = ?, world = ?, min_x = ?, min_y = ?, min_z = ?, max_x = ?, max_y = ?, max_z = ? WHERE id = ?",
          record.name(), record.world(), record.minX(), record.minY(), record.minZ(), record.maxX(),
          record.maxY(), record.maxZ(), record.id());
    } catch (SQLException ex) {
      logException(ex);
    }
  }

  @Override
  public void delete(long portalId) {
    try {
      DB.executeUpdate("DELETE FROM portals WHERE id = ?", portalId);
    } catch (SQLException ex) {
      logException(ex);
    }
  }

  @Override
  public void setLinkId(long portalId, @Nullable Long linkedPortalId) {
    try {
      DB.executeUpdate("UPDATE portals SET linked_portal_id = ? WHERE id = ?", linkedPortalId,
          portalId);
    } catch (SQLException ex) {
      logException(ex);
    }
  }

  private void logException(Exception ex) {
    dispatcher.error(ex.getMessage());
    ex.printStackTrace();
  }

  @Nullable
  private PortalRecord createRecord(DbRow row) {
    if (row == null) {
      return null;
    }

    var id = row.getInt("id");
    var name = row.getString("name");
    var world = row.getString("world");
    var minX = row.getInt("min_x");
    var minY = row.getInt("min_y");
    var minZ = row.getInt("min_z");
    var maxX = row.getInt("max_x");
    var maxY = row.getInt("max_y");
    var maxZ = row.getInt("max_z");

    var linkIdOptional = Optional.ofNullable((Number) row.get("linked_portal_id"));
    var linkId = linkIdOptional.isPresent() ? linkIdOptional.get().longValue() : null;

    return new PortalRecord(id, name, world, minX, minY, minZ, maxX, maxY, maxZ, linkId);
  }

  @Nullable
  private DbRow findRow(long id) {
    try {
      return DB.getFirstRow("SELECT * FROM portals WHERE id = ? LIMIT 1", id);
    } catch (SQLException ex) {
      logException(ex);
    }

    return null;
  }

  @Nullable
  private DbRow findRow(Region region) {
    try {
      return DB.getFirstRow(
          "SELECT * FROM portals WHERE world = ? AND min_x = ? AND min_y = ? AND min_z = ? AND max_x = ? AND max_y = ? AND max_z = ? LIMIT 1",
          region.worldName(), region.from().x(), region.from().y(), region.from().z(),
          region.to().x(), region.to().y(), region.to().z());
    } catch (SQLException ex) {
      logException(ex);
    }

    return null;
  }

  private void deleteOverlapping(Region region, long portalId) {
    try {
      List<Integer> results = DB.getFirstColumnResults(
          "SELECT id from portals WHERE ? = world AND ? <= max_x AND ? >= min_x AND ? <= max_y AND ? >= min_y AND ? <= max_z AND ? >= min_z AND id != ?",
          region.worldName(), region.from().x(), region.to().x(), region.from().y(),
          region.to().y(), region.from().z(), region.to().z(), portalId);

      for (Integer id : results) {
        DB.executeUpdate("DELETE FROM portals WHERE id = ?", id);
      }
    } catch (SQLException ex) {
      dispatcher.error(ex.getMessage());
      ex.printStackTrace();
    }
  }
}
