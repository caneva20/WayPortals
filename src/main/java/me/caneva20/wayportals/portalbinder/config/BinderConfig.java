package me.caneva20.wayportals.portalbinder.config;

import ch.jalu.configme.SettingsManager;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import javax.inject.Inject;
import lombok.val;
import me.caneva20.messagedispatcher.dispachers.IConsoleMessageDispatcher;
import me.caneva20.wayportals.settings.PluginConfig;
import org.bukkit.Material;

public class BinderConfig implements IBinderConfig {

  private final SettingsManager settingsManager;
  private final IConsoleMessageDispatcher dispatcher;

  private final ImmutableMap<Material, Float> materialDropMap;

  @Inject
  BinderConfig(SettingsManager settingsManager,
      IConsoleMessageDispatcher dispatcher) {
    this.settingsManager = settingsManager;
    this.dispatcher = dispatcher;

    materialDropMap = buildMaterialMap();
  }

  private ImmutableMap<Material, Float> buildMaterialMap() {
    val map = new HashMap<Material, Float>();
    final var materialsMap = settingsManager.getProperty(PluginConfig.DROP_MATERIALS);

    for (String materialStr : materialsMap.keySet()) {
      final var material = Material.getMaterial(materialStr.toUpperCase());

      if (material != null) {
        map.put(material, materialsMap.get(materialStr));
      } else {
        dispatcher.warn("Invalid configuration: %s is not a valid material name", materialStr);
      }
    }

    return ImmutableMap.copyOf(map);
  }

  @Override
  public ImmutableMap<Material, Float> dropMaterials() {
    return materialDropMap;
  }

  @Override
  public boolean dropWithSilkTouch() {
    return settingsManager.getProperty(PluginConfig.DROP_WITH_SILKTOUCH);
  }

  @Override
  public Material binderMaterial() {
    return settingsManager.getProperty(PluginConfig.BINDER_MATERIAL);
  }
}
