package me.caneva20.wayportals.events;

import me.caneva20.wayportals.items.portalbinder.PortalBinderUtility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import javax.inject.Inject;

public class InteractionEventHandler implements Listener {
    private final PortalBinderUtility binderUtility;

    @Inject
    InteractionEventHandler(
            PortalBinderUtility binderUtility
    ) {
        this.binderUtility = binderUtility;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        var item = event.getItemInHand();

        if (binderUtility.isBinder(item)) {
            event.setCancelled(true);
        }
    }
}
