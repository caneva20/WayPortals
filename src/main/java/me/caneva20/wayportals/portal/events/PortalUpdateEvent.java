package me.caneva20.wayportals.portal.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.caneva20.wayportals.utils.events.CancellableEvent;

@Getter
@RequiredArgsConstructor
public class PortalUpdateEvent extends CancellableEvent {

  private final long portalId;
}
