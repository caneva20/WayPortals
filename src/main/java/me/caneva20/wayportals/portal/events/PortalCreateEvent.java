package me.caneva20.wayportals.portal.events;

import lombok.RequiredArgsConstructor;
import me.caneva20.wayportals.utils.Region;
import me.caneva20.wayportals.utils.events.CancellableEvent;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class PortalCreateEvent extends CancellableEvent {

  @NotNull
  private final Region region;
}
