package me.caneva20.wayportals;

import co.aikar.commands.PaperCommandManager;
import javax.inject.Inject;
import me.caneva20.messagedispatcher.dispachers.IConsoleMessageDispatcher;
import me.caneva20.wayportals.commands.WayPortalsCommand;
import me.caneva20.wayportals.events.BindingEventHandler;
import me.caneva20.wayportals.events.InteractionEventHandler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class WayPortals extends JavaPlugin {

  @Inject
  private IConsoleMessageDispatcher dispatcher;

  @Inject
  private InteractionEventHandler interactionEventHandler;

  @Inject
  private BindingEventHandler bindingEventHandler;

  @Inject
  private PaperCommandManager commandManager;

  @Inject
  private PluginManager pluginManager;

  @Inject
  DatabaseHandler database;

  @Override
  public void onEnable() {
    var injector = new BinderModule(this).createInjector();

    injector.injectMembers(this);

    commandManager.registerCommand(injector.getInstance(WayPortalsCommand.class));

    pluginManager.registerEvents(interactionEventHandler, this);
    pluginManager.registerEvents(bindingEventHandler, this);

    database.initialize();

    dispatcher.success("Enabled! :)");
  }

  @Override
  public void onDisable() {
    database.close();

    dispatcher.success("Disabled :(");
  }
}
