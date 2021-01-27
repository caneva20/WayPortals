package me.caneva20.wayportals.portal.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.caneva20.wayportals.utils.events.BaseEvent;

@Getter
@AllArgsConstructor
public class PortalUpdatedEvent extends BaseEvent {
  private final long portalId;
}
