package me.caneva20.wayportals;

import co.aikar.commands.PaperCommandManager;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import me.caneva20.messagedispatcher.Messaging;
import me.caneva20.messagedispatcher.dispachers.IConsoleMessageDispatcher;
import me.caneva20.messagedispatcher.dispachers.IMessageDispatcher;
import me.caneva20.wayportals.commands.WayPortalsCommand;
import me.caneva20.wayportals.events.BindingEventHandler;
import me.caneva20.wayportals.events.InteractionEventHandler;
import me.caneva20.wayportals.events.TeleportEventHandler;
import me.caneva20.wayportals.portal.PortalModule;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Module
public class PluginModule {

  private final WayPortals plugin;

  public PluginModule(WayPortals plugin) {
    this.plugin = plugin;
  }

  @Provides
  @Singleton
  WayPortals provideWayPortals() {
    return plugin;
  }

  @Provides
  @Singleton
  JavaPlugin providePlugin() {
    return plugin;
  }

  @Provides
  @Singleton
  IConsoleMessageDispatcher provideConsoleMessageDispatcher() {
    return Messaging.createConsoleDispatcher(plugin, true);
  }

  @Provides
  @Singleton
  IMessageDispatcher provideMessageDispatcher() {
    return Messaging.createDispatcher(plugin, true);
  }

  @Provides
  @Singleton
  PaperCommandManager provideCommandManager() {
    return new PaperCommandManager(plugin);
  }

  @Provides
  @Singleton
  PluginManager providePluginManager() {
    return plugin.getServer().getPluginManager();
  }
}

@Singleton
@Component(modules = {PluginModule.class, PortalModule.class})
interface PluginComponent {

  //Plugin instances
  WayPortals getWayPortalsPlugin();

  JavaPlugin getJavaPlugin();

  //Logging
  IConsoleMessageDispatcher getConsoleMessageDispatcher();

  IMessageDispatcher getMessageDispatcher();

  //Others
  DatabaseHandler getDatabaseHandler();

  //Events
  InteractionEventHandler getInteractionEventHandler();

  BindingEventHandler getBindingEventHandler();

  TeleportEventHandler getTeleportEventHandler();

  //Commands
  PaperCommandManager getCommandManager();

  WayPortalsCommand getWayPortalsCommand();

  void inject(WayPortals plugin);
}
