package me.caneva20.wayportals.events;

import me.caneva20.messagedispatcher.dispachers.IMessageDispatcher;
import me.caneva20.wayportals.items.PortalBinderFactory;
import me.caneva20.wayportals.items.portalbinder.PortalBinderUtility;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.inject.Inject;

public class BindingEventHandler implements Listener {
    private final IMessageDispatcher dispatcher;
    private final PortalBinderFactory binderFactory;
    private final PortalBinderUtility binderUtility;

    @Inject
    BindingEventHandler(
            IMessageDispatcher dispatcher, PortalBinderFactory binderFactory, PortalBinderUtility binderUtility
    ) {
        this.dispatcher = dispatcher;
        this.binderFactory = binderFactory;
        this.binderUtility = binderUtility;
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

        var portalBinder = binderFactory.create(item);

        portalBinder.setTarget(block.getLocation());

        dispatcher.success(event.getPlayer(), "Portal binderFactory bound to this portal");
    }
}
