package me.caneva20.wayportals.portalbinder;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import java.io.File;
import javax.inject.Singleton;
import me.caneva20.wayportals.portalbinder.config.BinderConfig;
import me.caneva20.wayportals.portalbinder.config.BinderConfigManager;
import me.caneva20.wayportals.portalbinder.config.IBinderConfig;
import me.caneva20.wayportals.portalbinder.eventhandlers.DropEventHandler;
import me.caneva20.wayportals.portalbinder.eventhandlers.InteractionEventHandler;
import me.caneva20.wayportals.portalbinder.eventhandlers.PreventInteractionEventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@Module
public class PortalBinderModule {

  private final JavaPlugin plugin;

  public PortalBinderModule(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @Provides
  @Singleton
  @BinderConfigManager
  SettingsManager provideDropperConfigManager() {
    final var path = String.format("%s/config.yaml", plugin.getDataFolder());

    return SettingsManagerBuilder.withYamlFile(new File(path)).configurationData(BinderConfig.class)
        .useDefaultMigrationService().create();
  }

  @Provides
  @Singleton
  static IBinderConfig provideDropperConfig(BinderConfig impl) {
    return impl;
  }

  @Provides
  static IPortalBinderManager providePortalBinderManager(PortalBinderManager impl) {
    return impl;
  }

  @Provides
  @IntoSet
  static Listener provideDropEventHandler(DropEventHandler impl) {
    return impl;
  }

  @Provides
  @IntoSet
  static Listener provideInteractionEventHandler(InteractionEventHandler impl) {
    return impl;
  }

  @Provides
  @IntoSet
  static Listener providePreventInteractionEventHandler(PreventInteractionEventHandler impl) {
    return impl;
  }
}
