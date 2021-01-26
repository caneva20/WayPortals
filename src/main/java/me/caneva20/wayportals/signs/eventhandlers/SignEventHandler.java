package me.caneva20.wayportals.signs.eventhandlers;

import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

  @Nullable
  protected Sign getSign(@NotNull Block block) {
    if (!SIGNS.contains(block.getType())) {
      return null;
    }

    return (Sign) block.getState();
  }
}
