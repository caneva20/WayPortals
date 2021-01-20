package me.caneva20.wayportals.portal.events;

import lombok.RequiredArgsConstructor;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.utils.events.BaseEvent;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class PortalDeleteEvent extends BaseEvent {

  @NotNull
  private final Portal portal;
}
