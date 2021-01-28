package me.caneva20.wayportals.settings;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import javax.inject.Singleton;
import org.bukkit.plugin.java.JavaPlugin;

@Module
public class SettingsModule {

  private final JavaPlugin plugin;

  public SettingsModule(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @Provides
  @Singleton
  SettingsManager provideDropperConfigManager() {
    final var path = String.format("%s/config.yaml", plugin.getDataFolder());

    return SettingsManagerBuilder.withYamlFile(new File(path)).configurationData(PluginConfig.class)
        .useDefaultMigrationService().create();
  }
}
