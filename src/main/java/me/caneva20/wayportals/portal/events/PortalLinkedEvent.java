package me.caneva20.wayportals.portal.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.caneva20.wayportals.utils.events.BaseEvent;

@Getter
@RequiredArgsConstructor
public class PortalLinkedEvent extends BaseEvent {

  private final long sourceId;
  private final long destinationId;
}