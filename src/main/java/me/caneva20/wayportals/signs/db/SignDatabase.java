package me.caneva20.wayportals.signs.db;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import java.sql.SQLException;
import javax.inject.Inject;
import me.caneva20.messagedispatcher.dispachers.IConsoleMessageDispatcher;
import me.caneva20.wayportals.utils.WorldVector3;
import org.jetbrains.annotations.Nullable;

public class SignDatabase implements ISignDatabase {

  private final IConsoleMessageDispatcher dispatcher;

  @Inject
  SignDatabase(IConsoleMessageDispatcher dispatcher) {
    this.dispatcher = dispatcher;
  }

  @Override
  public void initialize() {
    try {
      DB.executeUpdate(
          "CREATE TABLE IF NOT EXISTS signs(id INTEGER NOT NULL, portal_id INTEGER NOT NULL, x INTEGER NOT NULL, y INTEGER NOT NULL, z INTEGER NOT NULL, world TEXT NOT NULL, PRIMARY KEY(id AUTOINCREMENT), FOREIGN KEY (portal_id) REFERENCES portals (id) ON DELETE CASCADE)");
      DB.executeUpdate("CREATE INDEX IF NOT EXISTS sign_id ON signs(id)");
    } catch (SQLException ex) {
      logException(ex);
    }
  }

  @Override
  @Nullable
  public SignRecord find(long id) {
    return createRecord(findRow(id));
  }

  @Override
  @Nullable
  public SignRecord find(WorldVector3 location) {
    return createRecord(findRow(location));
  }

  @Override
  @Nullable
  public SignRecord findForPortal(long portalId) {
    return createRecord(findPortalRow(portalId));
  }

  @Override
  @Nullable
  public SignRecord create(WorldVector3 location, int portalId) {
    try {
      var id = DB
          .executeInsert("INSERT INTO signs(portal_id, world, x, y, z) VALUES(?, ?, ?, ?, ?)",
              portalId, location.world(), location.x(), location.y(), location.z());

      return new SignRecord(id.intValue(), portalId, location.world(), (int) location.x(),
          (int) location.y(), (int) location.z());
    } catch (SQLException ex) {
      logException(ex);
    }

    return null;
  }

  @Override
  public void delete(long signId) {
    try {
      DB.executeUpdate("DELETE FROM signs WHERE id = ?", signId);
    } catch (SQLException ex) {
      logException(ex);
    }
  }

  @Nullable
  private SignRecord createRecord(DbRow row) {
    if (row == null) {
      return null;
    }

    var id = row.getInt("id");
    var portalId = row.getInt("portal_id");
    var world = row.getString("world");
    var x = row.getInt("x");
    var y = row.getInt("y");
    var z = row.getInt("z");

    return new SignRecord(id, portalId, world, x, y, z);
  }

  @Nullable
  private DbRow findRow(long id) {
    try {
      return DB.getFirstRow("SELECT * FROM signs WHERE id = ? LIMIT 1", id);
    } catch (SQLException ex) {
      logException(ex);
    }

    return null;
  }

  @Nullable
  private DbRow findRow(WorldVector3 location) {
    try {
      return DB
          .getFirstRow("SELECT * FROM signs WHERE world = ? AND x = ? AND y = ? AND z = ? LIMIT 1",
              location.world(), location.x(), location.y(), location.z());
    } catch (SQLException ex) {
      logException(ex);
    }

    return null;
  }

  @Nullable
  private DbRow findPortalRow(long portalId) {
    try {
      return DB.getFirstRow("SELECT * FROM signs WHERE portal_id = ? LIMIT 1", portalId);
    } catch (SQLException ex) {
      logException(ex);
    }

    return null;
  }

  private void logException(Exception ex) {
    dispatcher.error(ex.getMessage());
    ex.printStackTrace();
  }
}
