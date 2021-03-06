package me.caneva20.wayportals;

import javax.inject.Inject;
import javax.inject.Singleton;
import me.caneva20.messagedispatcher.dispachers.IConsoleMessageDispatcher;
import me.caneva20.wayportals.settings.SettingsModule;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Singleton
public final class WayPortals extends JavaPlugin {

  @Inject
  public IConsoleMessageDispatcher dispatcher;

  @Inject
  public PluginManager pluginManager;

  DatabaseHandler database;

  @Override
  public void onEnable() {
    var component = DaggerPluginComponent.builder().pluginModule(new PluginModule(this))
        .settingsModule(new SettingsModule(this)).build();

    component.inject(this);
    database = component.getDatabaseHandler();

    component.getCommandManager().registerCommand(component.getWayPortalsCommand());

    for (Listener handler : component.getEventHandlers()) {
      pluginManager.registerEvents(handler, this);
    }

    database.initialize();

    dispatcher.success("Enabled! :)");
  }

  @Override
  public void onDisable() {
    database.close();

    dispatcher.success("Disabled :(");
  }
}
