package me.caneva20.wayportals.portalbinder;

import java.util.ArrayList;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.caneva20.wayportals.portal.Portal;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class PortalBinder {

  private final ItemStack stack;

  @Setter(AccessLevel.PACKAGE)
  private Portal portal;

  PortalBinder(ItemStack stack) {
    this.stack = stack;

    updateLore();
  }

  public boolean hasPortal() {
    return portal != null;
  }

  public void updateLore() {
    var lore = new ArrayList<String>();

    lore.add("");

    if (portal != null) {
      lore.add(String.format("§eBound to: §6%s", portal.id()));
    } else {
      lore.add("§eBound to: §6(nowhere)");
    }

    lore.add("");

    var meta = getMeta();

    meta.setLore(lore);

    updateMeta(meta);
  }

  @NotNull
  private ItemMeta getMeta() {
    return Objects.requireNonNull(stack.getItemMeta());
  }

  private void updateMeta(ItemMeta meta) {
    stack.setItemMeta(meta);
  }
}
