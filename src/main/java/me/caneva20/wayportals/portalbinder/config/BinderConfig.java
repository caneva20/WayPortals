package me.caneva20.wayportals.portalbinder.config;

import static ch.jalu.configme.properties.PropertyInitializer.mapProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.properties.MapProperty;
import ch.jalu.configme.properties.Property;
import ch.jalu.configme.properties.types.PrimitivePropertyType;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import lombok.val;
import me.caneva20.messagedispatcher.dispachers.IConsoleMessageDispatcher;
import org.bukkit.Material;

public class BinderConfig implements SettingsHolder, IBinderConfig {

  @Comment("The material/type of the binder item")
  public static final Property<Material> BINDER_MATERIAL = newProperty(Material.class,
      "binder.binder-material", Material.PAPER);

  @Comment({
      "A list of materials the binder can drop from, followed the change of it being dropped.",
      "REDSTONE_ORE: 0.2 # Means that a binder can drop from a redstone ore with a 20% change"})
  public static final MapProperty<Float> DROP_MATERIALS = mapProperty(PrimitivePropertyType.FLOAT)
      .path("binder.drop-materials").defaultValue(Map.of(Material.REDSTONE_ORE.name(), .01f))
      .build();

  @Comment("If this is set to true, the binder will be able to drop event with a silk touch tool")
  public static final Property<Boolean> DROP_WITH_SILKTOUCH = newProperty(
      "binder.drop-with-silktouch", false);

  private final SettingsManager settingsManager;
  private final IConsoleMessageDispatcher dispatcher;

  private final ImmutableMap<Material, Float> materialDropMap;

  @Deprecated
  //This constructor is a requirement for ConfigMe
  public BinderConfig() {
    settingsManager = null;
    dispatcher = null;
    materialDropMap = null;
  }

  @Inject
  BinderConfig(@BinderConfigManager SettingsManager settingsManager,
      IConsoleMessageDispatcher dispatcher) {
    this.settingsManager = settingsManager;
    this.dispatcher = dispatcher;

    materialDropMap = buildMaterialMap();
  }

  private ImmutableMap<Material, Float> buildMaterialMap() {
    val map = new HashMap<Material, Float>();
    final var materialsMap = settingsManager.getProperty(DROP_MATERIALS);

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
    return settingsManager.getProperty(DROP_WITH_SILKTOUCH);
  }

  @Override
  public Material binderMaterial() {
    return settingsManager.getProperty(BINDER_MATERIAL);
  }
}
