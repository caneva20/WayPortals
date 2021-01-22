package me.caneva20.wayportals.utils;

import java.util.Arrays;
import java.util.stream.Stream;
import lombok.val;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class InventoryUtility {

  public static boolean withdraw(Inventory inventory, ItemStack stack, int amount) {
    val stacks = Arrays.stream(inventory.getContents()).filter(x -> x != null && x.equals(stack));

    return withdraw(stacks, amount);
  }

  public static boolean withdraw(Inventory inventory, Material material, int amount) {
    val stacks = Arrays.stream(inventory.getContents()).filter(x -> x.getType() == material);

    return withdraw(stacks, amount);
  }

  public static boolean withdraw(Player player, ItemStack stack, int amount) {
    if (player.getGameMode().equals(GameMode.CREATIVE)) {
      return true;
    }

    return withdraw(player.getInventory(), stack, amount);
  }

  public static boolean withdraw(Player player, Material material, int amount) {
    if (player.getGameMode().equals(GameMode.CREATIVE)) {
      return true;
    }

    return withdraw(player.getInventory(), material, amount);
  }

  private static boolean withdraw(Stream<ItemStack> stacks, int amount) {
    val stack = stacks.filter(x -> x.getAmount() >= amount).findFirst();

    if (stack.isEmpty()) {
      return false;
    }

    stack.get().setAmount(stack.get().getAmount() - amount);

    return true;
  }

}
