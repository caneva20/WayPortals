package me.caneva20.wayportals;

import co.aikar.commands.PaperCommandManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import java.util.Set;
import javax.inject.Singleton;
import me.caneva20.messagedispatcher.Messaging;
import me.caneva20.messagedispatcher.dispachers.IConsoleMessageDispatcher;
import me.caneva20.messagedispatcher.dispachers.IMessageDispatcher;
import me.caneva20.wayportals.commands.WayPortalsCommand;
import me.caneva20.wayportals.events.BindingEventHandler;
import me.caneva20.wayportals.events.InteractionEventHandler;
import me.caneva20.wayportals.events.TeleportEventHandler;
import me.caneva20.wayportals.portal.PortalModule;
import me.caneva20.wayportals.portalbinder.BinderEventHandler;
import me.caneva20.wayportals.portalbinder.PortalBinderModule;
import me.caneva20.wayportals.portalbinder.config.IBinderConfig;
import me.caneva20.wayportals.signs.SignModule;
import me.caneva20.wayportals.utils.SignMenuFactory;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Singleton
@Component(modules = {PluginModule.class, PortalModule.class, SignModule.class,
    PortalBinderModule.class})
interface PluginComponent {

  //Plugin instances
  WayPortals getWayPortalsPlugin();

  JavaPlugin getJavaPlugin();

  //Logging
  IConsoleMessageDispatcher getConsoleMessageDispatcher();

  IMessageDispatcher getMessageDispatcher();

  //Others
  DatabaseHandler getDatabaseHandler();

  ProtocolManager getProtocolManager();

  SignMenuFactory getSignMenuFactory();

  //Events
  Set<Listener> getEventHandlers();

  InteractionEventHandler getInteractionEventHandler();

  BindingEventHandler getBindingEventHandler();

  TeleportEventHandler getTeleportEventHandler();

  BinderEventHandler getBinderEventHandler();

  //Commands
  PaperCommandManager getCommandManager();

  WayPortalsCommand getWayPortalsCommand();

  //Configs
  IBinderConfig getDropperConfig();

  void inject(WayPortals plugin);
}

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
    return Messaging.createConsoleDispatcher(plugin);
  }

  @Provides
  @Singleton
  IMessageDispatcher provideMessageDispatcher() {
    return Messaging.createDispatcher(plugin);
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

  @Provides
  @Singleton
  ProtocolManager provideProtocolManager() {
    return ProtocolLibrary.getProtocolManager();
  }

  @Provides
  @Singleton
  SignMenuFactory provideSignMenuFactory() {
    return new SignMenuFactory(plugin);
  }
}
