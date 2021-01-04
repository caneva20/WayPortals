package me.caneva20.wayportals;

import co.aikar.idb.DB;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.PooledDatabaseOptions;
import javax.inject.Singleton;
import org.bukkit.plugin.java.JavaPlugin;

@Singleton
public class DatabaseHandler {

  public void initialize(JavaPlugin plugin) {
    var dataFolder = plugin.getDataFolder();

    if (!dataFolder.exists()) {
      dataFolder.mkdirs();
    }

    var options = DatabaseOptions.builder().sqlite(dataFolder + "\\db.db").build();
    var db = PooledDatabaseOptions.builder().options(options).createHikariDatabase();

    DB.setGlobalDatabase(db);
  }

  public void close() {
    DB.close();
  }
}
