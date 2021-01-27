package me.caneva20.wayportals.portal.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.caneva20.wayportals.utils.events.BaseEvent;
import org.jetbrains.annotations.Nullable;

@Getter
@RequiredArgsConstructor
public class PortalUnlinkedEvent extends BaseEvent {

  private final long sourceId;

  @Nullable
  private final Long oldLinkId;
}
