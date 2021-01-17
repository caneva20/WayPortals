package me.caneva20.wayportals;

import co.aikar.idb.DB;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.PooledDatabaseOptions;
import javax.inject.Inject;
import javax.inject.Singleton;
import me.caneva20.messagedispatcher.dispachers.IConsoleMessageDispatcher;
import me.caneva20.wayportals.portal.db.IPortalDatabase;
import me.caneva20.wayportals.signs.db.ISignDatabase;
import org.bukkit.plugin.java.JavaPlugin;

@Singleton
public class DatabaseHandler {

  private final JavaPlugin plugin;
  private final IConsoleMessageDispatcher dispatcher;
  private final IPortalDatabase portalDatabase;
  private final ISignDatabase signDatabase;

  @Inject
  DatabaseHandler(JavaPlugin plugin, IConsoleMessageDispatcher dispatcher,
      IPortalDatabase portalDatabase, ISignDatabase signDatabase) {
    this.plugin = plugin;
    this.dispatcher = dispatcher;
    this.portalDatabase = portalDatabase;
    this.signDatabase = signDatabase;
  }

  public void initialize() {
    var dataFolder = plugin.getDataFolder();

    if (!dataFolder.exists()) {
      //noinspection ResultOfMethodCallIgnored
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
    portalDatabase.initialize();
    signDatabase.initialize();
    dispatcher.success("Database created!");
  }
}
