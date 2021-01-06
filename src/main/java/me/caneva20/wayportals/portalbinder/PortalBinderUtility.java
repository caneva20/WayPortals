package me.caneva20.wayportals.portalbinder;

import me.caneva20.wayportals.KeyProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;

@Singleton
public class PortalBinderUtility {

  private final KeyProvider keys;

  @Inject
  PortalBinderUtility(KeyProvider keys) {
    this.keys = keys;
  }

  public boolean isBinder(ItemStack stack) {
    if (!stack.hasItemMeta()) {
      return false;
    }

    return Objects.requireNonNull(stack.getItemMeta())
        .getPersistentDataContainer()
        .has(keys.getPortalBinderKey(), PersistentDataType.INTEGER);
  }

  public boolean hasBinderInHand(Player player) {
    return isBinder(player.getInventory().getItemInMainHand()) ||
        isBinder(player.getInventory().getItemInOffHand());
  }
}
