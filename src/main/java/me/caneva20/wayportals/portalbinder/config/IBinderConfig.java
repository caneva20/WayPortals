package me.caneva20.wayportals.portalbinder.config;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;

public interface IBinderConfig {

  ImmutableMap<Material, Float> dropMaterials();

  boolean dropWithSilkTouch();

  Material binderMaterial();
}
