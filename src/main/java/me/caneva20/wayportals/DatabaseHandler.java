package me.caneva20.wayportals;

import co.aikar.idb.DB;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.PooledDatabaseOptions;
import java.sql.SQLException;
import javax.inject.Inject;
import javax.inject.Singleton;
import me.caneva20.messagedispatcher.dispachers.IConsoleMessageDispatcher;
import org.bukkit.plugin.java.JavaPlugin;

@Singleton
public class DatabaseHandler {

  private final JavaPlugin plugin;
  private final IConsoleMessageDispatcher dispatcher;

  @Inject
  DatabaseHandler(JavaPlugin plugin,
      IConsoleMessageDispatcher dispatcher) {
    this.plugin = plugin;
    this.dispatcher = dispatcher;
  }

  public void initialize() {
    var dataFolder = plugin.getDataFolder();

    if (!dataFolder.exists()) {
      dataFolder.mkdirs();
    }

    var options = DatabaseOptions.builder().sqlite(dataFolder + "\\db.db").build();
    var db = PooledDatabaseOptions.builder().options(options).createHikariDatabase();

    DB.setGlobalDatabase(db);

    createDatabase();
  }

  public void close() {
    DB.close();
  }

  private void createDatabase() {
    try {
      DB.executeUpdate("CREATE TABLE IF NOT EXISTS 'portals'( 'id' INTEGER NOT NULL, 'id_string' TEXT NOT NULL, 'linked_portal_id' INTEGER, 'from_x' INTEGER NOT NULL DEFAULT 0, 'from_y' INTEGER NOT NULL DEFAULT 0, 'from_z' INTEGER NOT NULL DEFAULT 0, 'to_x' INTEGER NOT NULL DEFAULT 0, 'to_y' INTEGER NOT NULL DEFAULT 0, 'to_z' INTEGER NOT NULL DEFAULT 0, FOREIGN KEY('linked_portal_id') REFERENCES 'portals'('id'), PRIMARY KEY('id' AUTOINCREMENT));");
      DB.executeUpdate("CREATE INDEX IF NOT EXISTS 'portal_id' ON 'portals'( 'id', 'from_x');");
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    dispatcher.success("Database created!");
  }
}
