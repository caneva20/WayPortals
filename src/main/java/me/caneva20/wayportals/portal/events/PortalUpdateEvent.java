package me.caneva20.wayportals.portal.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.utils.events.CancellableEvent;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class PortalUpdateEvent extends CancellableEvent {

  @NotNull
  private final Portal portal;
}
