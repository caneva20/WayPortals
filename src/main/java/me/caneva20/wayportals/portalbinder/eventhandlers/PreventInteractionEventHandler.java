package me.caneva20.wayportals.portalbinder.eventhandlers;

import javax.inject.Inject;
import me.caneva20.wayportals.portalbinder.IPortalBinderManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PreventInteractionEventHandler implements Listener {

  private final IPortalBinderManager manager;

  @Inject
  PreventInteractionEventHandler(IPortalBinderManager manager) {
    this.manager = manager;
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event) {
    var item = event.getItemInHand();

    if (manager.isBinder(item)) {
      event.setCancelled(true);
    }
  }
}
