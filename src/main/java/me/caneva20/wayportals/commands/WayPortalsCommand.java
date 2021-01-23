package me.caneva20.wayportals.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import lombok.val;
import me.caneva20.messagedispatcher.dispachers.IMessageDispatcher;
import me.caneva20.wayportals.portalbinder.IPortalBinderManager;
import me.caneva20.wayportals.portalbinder.PortalBinderUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

@CommandAlias("wayportals|wp|wayp")
public class WayPortalsCommand extends BaseCommand {

  private final IMessageDispatcher dispatcher;
  private final PortalBinderUtility binderUtility;
  private final IPortalBinderManager binderManager;

  @Inject
  WayPortalsCommand(IMessageDispatcher dispatcher, PortalBinderUtility binderUtility,
      IPortalBinderManager binderManager) {
    this.dispatcher = dispatcher;
    this.binderUtility = binderUtility;
    this.binderManager = binderManager;
  }

  @Subcommand("item")
  public void onItem(Player sender, Material material) {
    var stack = new ItemStack(material);

    binderManager.get(stack);

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

    val portal = binderManager.get(stack).portal();

    dispatcher.debug(player, "Location: " + (portal != null ? portal.toString() : "nowhere"));
  }
}
