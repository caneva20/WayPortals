package me.caneva20.wayportals.portal.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.caneva20.wayportals.utils.events.CancellableEvent;

@Getter
@RequiredArgsConstructor
public class PortalLinkEvent extends CancellableEvent {

  private final long sourceId;
  private final long destinationId;
}
