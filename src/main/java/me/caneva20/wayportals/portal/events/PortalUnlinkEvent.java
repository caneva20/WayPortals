package me.caneva20.wayportals.portal.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.utils.events.BaseEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@RequiredArgsConstructor
public class PortalUnlinkEvent extends BaseEvent {

  @NotNull
  private final Portal source;

  @Nullable
  private final Portal destination;
}
