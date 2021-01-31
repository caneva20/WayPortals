package me.caneva20.wayportals.portalbinder.eventhandlers;

import javax.inject.Inject;
import me.caneva20.messagedispatcher.dispachers.IMessageDispatcher;
import me.caneva20.wayportals.portal.IPortalManager;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.portalbinder.IPortalBinderManager;
import me.caneva20.wayportals.portalbinder.PortalBinder;
import me.caneva20.wayportals.utils.InventoryUtility;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InteractionEventHandler implements Listener {

  private final IPortalBinderManager binderManager;
  private final IPortalManager portalManager;
  private final IMessageDispatcher dispatcher;

  @Inject
  InteractionEventHandler(IPortalBinderManager binderManager, IPortalManager portalManager,
      IMessageDispatcher dispatcher) {
    this.binderManager = binderManager;
    this.portalManager = portalManager;
    this.dispatcher = dispatcher;
  }

  private void bindTarget(Player player, PortalBinder binder, @NotNull Portal portal) {
    ItemStack stack = binder.stack().clone();

    InventoryUtility.withdraw(player, binder.stack(), 1);

    binder = binderManager.get(stack);

    if (binder == null) {
      return;
    }

    binderManager.setPortal(binder, portal);

    InventoryUtility.deposit(player, stack, 1);
  }

  private void linkPortals(Player player, PortalBinder binder, @NotNull Portal portal) {
    if (!binder.hasPortal()) {
      return;
    }

    if (binder.portal().id() == portal.id()) {
      dispatcher.warn(player, "You bind a portal to itself!");

      return;
    }

    if (!InventoryUtility.withdraw(player, binder.stack(), 1)) {
      return;
    }

    portalManager.link(portal, binder.portal());
  }

  @Nullable
  private Portal findOrCreatePortal(Block portalBlock, Player player) {
    var portal = portalManager.get(portalBlock.getLocation());

    if (portal == null) {
      return portalManager.create(portalBlock.getLocation(), player);
    }

    return portal;
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onInteract(PlayerInteractEvent event) {
    var item = event.getItem();

    if (event.getAction() != Action.RIGHT_CLICK_BLOCK || item == null) {
      return;
    }

    var block = event.getClickedBlock();

    if (block == null || block.getType() != Material.NETHER_PORTAL) {
      return;
    }

    var binder = binderManager.get(item);

    if (binder == null) {
      return;
    }

    var portal = findOrCreatePortal(block, event.getPlayer());

    if (portal == null) {
      return;
    }

    if (!binder.hasPortal()) {
      bindTarget(event.getPlayer(), binder, portal);
    } else {
      linkPortals(event.getPlayer(), binder, portal);
    }

    event.setCancelled(true);
  }
}
