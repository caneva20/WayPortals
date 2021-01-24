package me.caneva20.wayportals.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import javax.inject.Inject;
import lombok.val;
import me.caneva20.messagedispatcher.dispachers.IMessageDispatcher;
import me.caneva20.wayportals.portalbinder.IPortalBinderManager;
import org.bukkit.entity.Player;

@CommandAlias("wayportals|wp|wayp")
public class WayPortalsCommand extends BaseCommand {

  private final IMessageDispatcher dispatcher;
  private final IPortalBinderManager binderManager;

  @Inject
  WayPortalsCommand(IMessageDispatcher dispatcher, IPortalBinderManager binderManager) {
    this.dispatcher = dispatcher;
    this.binderManager = binderManager;
  }

  @Subcommand("item")
  public void onItem(Player sender) {
    val binder = binderManager.create();

    sender.getInventory().addItem(binder.stack());

    dispatcher.success(sender, "Here's your item!");
  }
}
