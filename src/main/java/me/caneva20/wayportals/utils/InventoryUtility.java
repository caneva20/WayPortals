package me.caneva20.wayportals.utils;

import java.util.Arrays;
import lombok.val;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public final class InventoryUtility {

    public static boolean withdraw(Inventory inventory, Material material, int amount) {
    if (!inventory.contains(material)) {
      return false;
    }

    val itemStack = Arrays.stream(inventory.getContents())
        .filter(x -> x.getType() == material && x.getAmount() >= amount).findFirst();

    if (itemStack.isEmpty()) {
      return false;
    }

    itemStack.get().setAmount(itemStack.get().getAmount() - amount);

    return true;
  }

  public static boolean withdraw(Player player, Material material, int amount) {
    if (player.getGameMode().equals(GameMode.CREATIVE)) {
      return true;
    }

    return withdraw(player.getInventory(), material, amount);
  }

}
