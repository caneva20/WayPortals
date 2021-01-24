package me.caneva20.wayportals.portalbinder;

import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.val;
import me.caneva20.messagedispatcher.dispachers.IConsoleMessageDispatcher;
import me.caneva20.wayportals.KeyProvider;
import me.caneva20.wayportals.portal.IPortalManager;
import me.caneva20.wayportals.portal.Portal;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class PortalBinderManager implements IPortalBinderManager {

  private final KeyProvider keys;
  private final IConsoleMessageDispatcher dispatcher;
  private final IPortalManager portalManager;
  private final PortalBinderUtility binderUtility;

  @Inject
  PortalBinderManager(KeyProvider keys, IConsoleMessageDispatcher dispatcher,
      IPortalManager portalManager, PortalBinderUtility binderUtility) {
    this.keys = keys;
    this.dispatcher = dispatcher;
    this.portalManager = portalManager;
    this.binderUtility = binderUtility;
  }

  @Override
  @Nullable
  public PortalBinder get(ItemStack stack) {
    if (!binderUtility.isBinder(stack)) {
      transform(stack);
    }

    val binder = new PortalBinder(stack);

    var portalId = getPortalId(stack);

    if (portalId != null) {
      binder.portal(portalManager.find(portalId));
    }

    binder.updateLore();

    return binder;
  }

  @Override
  public void setPortal(@NotNull ItemStack stack, @NotNull Portal portal) {
    var meta = getMeta(stack);

    meta.getPersistentDataContainer()
        .set(keys.getPortalBinderTargetKey(), PersistentDataType.LONG, portal.id());

    stack.setItemMeta(meta);

    val binder = get(stack);

    binder.portal(portal);

    binder.updateLore();
  }

  @Override
  public void setPortal(@NotNull PortalBinder binder, @NotNull Portal portal) {
    setPortal(binder.stack(), portal);
  }

  @NotNull
  private ItemMeta getMeta(@NotNull ItemStack stack) {
    return Objects.requireNonNull(stack.getItemMeta());
  }

  @Nullable
  private Long getPortalId(@NotNull ItemStack stack) {
    var key = keys.getPortalBinderTargetKey();

    var container = getMeta(stack).getPersistentDataContainer();

    return container.get(key, PersistentDataType.LONG);
  }

  private void transform(ItemStack stack) {
    var portalBinderKey = keys.getPortalBinderKey();

    var meta = Bukkit.getItemFactory().getItemMeta(stack.getType());

    if (meta == null) {
      dispatcher.error("%s can't be used as a portal binder!", stack.getType());

      return;
    }

    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

    meta.getPersistentDataContainer().set(portalBinderKey, PersistentDataType.INTEGER, 1);

    stack.setItemMeta(meta);
    stack.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
  }
}
