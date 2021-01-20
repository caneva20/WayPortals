package me.caneva20.wayportals.utils.events;

import org.bukkit.event.Cancellable;

public class CancellableEvent extends BaseEvent implements Cancellable {

  private boolean cancel;

  @Override
  public boolean isCancelled() {
    return cancel;
  }

  @Override
  public void setCancelled(boolean cancel) {
    this.cancel = cancel;
  }
}
