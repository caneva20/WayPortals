package me.caneva20.wayportals.portal.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.utils.events.BaseEvent;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class PortalLinkedEvent extends BaseEvent {

  @NotNull
  private final Portal source;

  @NotNull
  private final Portal destination;
}