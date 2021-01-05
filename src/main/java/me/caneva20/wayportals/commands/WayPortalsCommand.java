package me.caneva20.wayportals.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import me.caneva20.messagedispatcher.dispachers.IMessageDispatcher;
import me.caneva20.wayportals.portalbinder.PortalBinderFactory;
import me.caneva20.wayportals.portalbinder.PortalBinderUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

@CommandAlias("wayportals|wp|wayp")
public class WayPortalsCommand extends BaseCommand {
    private final IMessageDispatcher dispatcher;
    private final PortalBinderUtility binderUtility;
    private final PortalBinderFactory binderFactory;

    @Inject
    WayPortalsCommand(
            IMessageDispatcher dispatcher, PortalBinderUtility binderUtility, PortalBinderFactory binderFactory
    ) {
        this.dispatcher = dispatcher;
        this.binderUtility = binderUtility;
        this.binderFactory = binderFactory;
    }

    @Subcommand("item")
    public void onItem(Player sender, Material material) {
        var stack = new ItemStack(material);

        binderFactory.create(stack);

        sender.getInventory().addItem(stack);

        dispatcher.success(sender, "Here's your item!");
    }

    @Subcommand("dump")
    public void onDump(Player player) {
        var stack = player.getInventory().getItemInMainHand();

        if (!binderUtility.isBinder(stack)) {
            dispatcher.error(player, "This item is not a portal binder");

            return;
        }

        var location = binderFactory.create(stack).getPortal();

        dispatcher.debug(player, "Location: " + (location != null ? location.toString() : "nowhere"));
    }
}
