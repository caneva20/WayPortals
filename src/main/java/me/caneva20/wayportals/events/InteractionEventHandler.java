package me.caneva20.wayportals.events;

import javax.inject.Inject;
import me.caneva20.wayportals.portalbinder.IPortalBinderManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class InteractionEventHandler implements Listener {

  private final IPortalBinderManager manager;

  @Inject
  InteractionEventHandler(IPortalBinderManager manager) {
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
