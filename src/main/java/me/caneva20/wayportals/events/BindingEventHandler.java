package me.caneva20.wayportals.events;

import com.google.common.base.Stopwatch;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import me.caneva20.messagedispatcher.dispachers.IMessageDispatcher;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.portal.PortalUtility;
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

  private final IMessageDispatcher dispatcher;
  private final PortalBinderFactory binderFactory;
  private final PortalBinderUtility binderUtility;

  @Inject
  BindingEventHandler(
      IMessageDispatcher dispatcher,
      PortalBinderFactory binderFactory,
      PortalBinderUtility binderUtility) {
    this.dispatcher = dispatcher;
    this.binderFactory = binderFactory;
    this.binderUtility = binderUtility;
  }

  private void bindTarget(Player player, PortalBinder binder, @NotNull Portal portal) {
    binder.setPortal(portal);
    dispatcher.success(player, "Portal binder bound to this portal");
  }

  private void linkPortals(Player player, PortalBinder binder, @NotNull Portal portal) {
    player.getInventory().remove(binder.getStack());

    dispatcher.debug(player, portal.toString());
    dispatcher.debug(player, String.format("ID: %s", portal.id()));
  }

  private @Nullable Portal findPortal(Block portalBlock, Player player) {
    var stopwatch = Stopwatch.createStarted();
    var portal = PortalUtility.find(portalBlock);

    stopwatch.stop();

    dispatcher.debug(player, String.format("Find portal took %sms (%sμs) to run",
        stopwatch.elapsed(TimeUnit.MILLISECONDS),
        stopwatch.elapsed(TimeUnit.MICROSECONDS)));

    return portal;
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

    var portal = findPortal(block, event.getPlayer());

    var binder = binderFactory.create(item);

    if (portal == null) {
      dispatcher.debug(event.getPlayer(), "Portal not found here");

      return;
    }

    if (!binder.hasPortal()) {
      dispatcher.debug(event.getPlayer(), "Binding to portal " + portal.id());

      bindTarget(event.getPlayer(), binder, portal);
    } else {
      dispatcher.debug(event.getPlayer(), String.format("Binding portal %s to %s", binder.getPortal().id(), portal.id()));

      linkPortals(event.getPlayer(), binder, portal);
    }

    event.setCancelled(true);
  }
}
