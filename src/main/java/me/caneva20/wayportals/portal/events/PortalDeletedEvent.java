package me.caneva20.wayportals.portal.events;

import lombok.RequiredArgsConstructor;
import me.caneva20.wayportals.utils.events.BaseEvent;

@RequiredArgsConstructor
public class PortalDeletedEvent extends BaseEvent {

  private final long portalId;
}
