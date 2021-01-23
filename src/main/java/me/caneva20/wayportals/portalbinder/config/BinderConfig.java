package me.caneva20.wayportals.portalbinder.config;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.properties.Property;
import javax.inject.Inject;
import org.bukkit.Material;

public class BinderConfig implements SettingsHolder, IBinderConfig {

  @Comment("The material/type of the binder item")
  public static final Property<Material> BINDER_MATERIAL = newProperty(Material.class,
      "binder.binder-material", Material.PAPER);

  private final SettingsManager settingsManager;

  @Deprecated
  //This constructor is a requirement for ConfigMe
  public BinderConfig() {
    settingsManager = null;
  }

  @Inject
  BinderConfig(@BinderConfigManager SettingsManager settingsManager) {
    this.settingsManager = settingsManager;
  }

  @Override
  public Material binderMaterial() {
    return settingsManager.getProperty(BINDER_MATERIAL);
  }
}
