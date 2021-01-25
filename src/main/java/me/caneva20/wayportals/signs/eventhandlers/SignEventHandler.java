package me.caneva20.wayportals.signs.eventhandlers;

import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.event.Listener;

public abstract class SignEventHandler implements Listener {

  protected static final Set<Material> SIGNS = Set
      .of(Material.OAK_WALL_SIGN, Material.SPRUCE_WALL_SIGN, Material.BIRCH_WALL_SIGN,
          Material.JUNGLE_WALL_SIGN, Material.ACACIA_WALL_SIGN, Material.DARK_OAK_WALL_SIGN,
          Material.CRIMSON_WALL_SIGN, Material.WARPED_WALL_SIGN);

  protected static final Map<Material, Material> SIGN_MATERIAL_MAP = Map
      .of(Material.OAK_WALL_SIGN, Material.OAK_SIGN, Material.SPRUCE_WALL_SIGN,
          Material.SPRUCE_SIGN, Material.BIRCH_WALL_SIGN, Material.BIRCH_SIGN,
          Material.JUNGLE_WALL_SIGN, Material.JUNGLE_SIGN, Material.ACACIA_WALL_SIGN,
          Material.ACACIA_SIGN, Material.DARK_OAK_WALL_SIGN, Material.DARK_OAK_SIGN,
          Material.CRIMSON_WALL_SIGN, Material.CRIMSON_SIGN, Material.WARPED_WALL_SIGN,
          Material.WARPED_SIGN);
}
