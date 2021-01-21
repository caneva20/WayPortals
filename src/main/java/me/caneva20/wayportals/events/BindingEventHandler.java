package me.caneva20.wayportals.events;

import javax.inject.Inject;
import me.caneva20.wayportals.portal.IPortalManager;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.portalbinder.PortalBinder;
import me.caneva20.wayportals.portalbinder.PortalBinderFactory;
import me.caneva20.wayportals.portalbinder.PortalBinderUtility;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BindingEventHandler implements Listener {

  private final PortalBinderFactory binderFactory;
  private final PortalBinderUtility binderUtility;
  private final IPortalManager portalManager;

  @Inject
  BindingEventHandler(PortalBinderFactory binderFactory, PortalBinderUtility binderUtility,
      IPortalManager portalManager) {
    this.binderFactory = binderFactory;
    this.binderUtility = binderUtility;
    this.portalManager = portalManager;
  }

  private void bindTarget(PortalBinder binder, @NotNull Portal portal) {
    binder.setPortal(portal);
  }

  private void linkPortals(Player player, PortalBinder binder, @NotNull Portal portal) {
    player.getInventory().remove(binder.getStack());

    if (binder.hasPortal()) {
      portalManager.link(portal, binder.getPortal());
    }
  }

  private @Nullable Portal findPortal(Block portalBlock) {
    return portalManager.get(portalBlock.getLocation());
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onInteract(PlayerInteractEvent event) {
    var item = event.getItem();

    if (event.getAction() != Action.RIGHT_CLICK_BLOCK || item == null) {
      return;
    }

    var block = event.getClickedBlock();

    if (block == null || block.getType() != Material.NETHER_PORTAL) {
      return;
    }

    if (!binderUtility.isBinder(item)) {
      return;
    }

    var portal = findPortal(block);

    var binder = binderFactory.create(item);

    if (portal == null) {
      return;
    }

    if (!binder.hasPortal()) {
      bindTarget(binder, portal);
    } else {
      linkPortals(event.getPlayer(), binder, portal);
    }

    event.setCancelled(true);
  }
}
