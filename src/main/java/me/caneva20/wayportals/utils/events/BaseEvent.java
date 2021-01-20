package me.caneva20.wayportals.utils.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class BaseEvent extends Event {

  private static final HandlerList handlers = new HandlerList();

  @NotNull
  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
