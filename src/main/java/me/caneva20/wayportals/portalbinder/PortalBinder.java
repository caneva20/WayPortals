package me.caneva20.wayportals.portalbinder;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import java.util.ArrayList;
import java.util.Objects;
import javax.inject.Inject;
import me.caneva20.messagedispatcher.dispachers.IConsoleMessageDispatcher;
import me.caneva20.wayportals.KeyProvider;
import me.caneva20.wayportals.WayPortals;
import me.caneva20.wayportals.portal.IPortalManager;
import me.caneva20.wayportals.portal.Portal;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoFactory
public class PortalBinder implements IPortalBinder {

  private final WayPortals plugin;
  private final KeyProvider keys;
  private final IConsoleMessageDispatcher consoleDispatcher;

  private final ItemStack stack;

  private Portal portal;

  @Inject
  PortalBinder(@Provided WayPortals plugin, @Provided KeyProvider keys,
      @Provided PortalBinderUtility binderUtility,
      @Provided IConsoleMessageDispatcher consoleDispatcher, @Provided IPortalManager portalManager,
      ItemStack stack) {
    this.plugin = plugin;
    this.keys = keys;
    this.consoleDispatcher = consoleDispatcher;
    this.stack = stack;

    if (!binderUtility.isBinder(stack)) {
      transform();
    }

    if (hasPortal()) {
      var portalId = getPortalId();

      if (portalId != null) {
        //TODO: Change portal id to int

        portal = portalManager.find(portalId.intValue());
      }
    }
  }

  public ItemStack getStack() {
    return stack;
  }

  private void transform() {
    var portalBinderKey = keys.getPortalBinderKey();

    var meta = plugin.getServer().getItemFactory().getItemMeta(stack.getType());

    if (meta == null) {
      consoleDispatcher.error("This item stack can't be transformed into a portal binder!");

      return;
    }

    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

    meta.getPersistentDataContainer().set(portalBinderKey, PersistentDataType.INTEGER, 1);

    stack.setItemMeta(meta);
    stack.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);

    updateLore();
  }

  @NotNull
  private ItemMeta getMeta() {
    return Objects.requireNonNull(stack.getItemMeta());
  }

  private boolean updateMeta(ItemMeta meta) {
    return stack.setItemMeta(meta);
  }

  private void updateLore() {
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

  @Nullable
  private Long getPortalId() {
    var key = keys.getPortalBinderTargetKey();

    var container = getMeta().getPersistentDataContainer();

    return container.get(key, PersistentDataType.LONG);
  }

  @Nullable
  @Override
  public Portal getPortal() {
    return portal;
  }

  public void setPortal(Portal portal) {
    var meta = getMeta();

    meta.getPersistentDataContainer().set(keys.getPortalBinderTargetKey(), PersistentDataType.LONG,
        portal.id());

    this.portal = portal;

    updateMeta(meta);
    updateLore();
  }

  public boolean hasPortal() {
    var key = keys.getPortalBinderTargetKey();

    var container = getMeta().getPersistentDataContainer();

    return container.has(key, PersistentDataType.LONG);
  }
}
