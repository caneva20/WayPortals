package me.caneva20.wayportals.settings;

import static ch.jalu.configme.properties.PropertyInitializer.mapProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.MapProperty;
import ch.jalu.configme.properties.Property;
import ch.jalu.configme.properties.types.PrimitivePropertyType;
import java.util.Map;
import org.bukkit.Material;

public class PluginConfig implements SettingsHolder {
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
}
