package me.caneva20.wayportals.portal.events;

import lombok.RequiredArgsConstructor;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.utils.events.BaseEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class PortalUnlinkEvent extends BaseEvent {

  @NotNull
  private final Portal source;

  @Nullable
  private final Portal destination;
}
