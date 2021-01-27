package me.caneva20.wayportals.signs.eventhandlers;

import javax.inject.Inject;
import lombok.CustomLog;
import lombok.val;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.portal.events.PortalLinkedEvent;
import me.caneva20.wayportals.portal.events.PortalUnlinkEvent;
import me.caneva20.wayportals.portal.events.PortalUpdateEvent;
import me.caneva20.wayportals.signs.SignManager;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.Nullable;

@CustomLog
public class PortalUpdateSignEventHandler extends SignEventHandler {

  private final SignManager signManager;

  @Inject
  PortalUpdateSignEventHandler(SignManager signManager) {
    this.signManager = signManager;
  }

  @EventHandler
  private void onLink(PortalLinkedEvent event) {
    updatePortal(event.source());
    updatePortal(event.destination());
  }

  @EventHandler
  private void onUnlink(PortalUnlinkEvent event) {
    updatePortal(event.source());
    updatePortal(event.destination());
  }

  @EventHandler
  private void onUpdate(PortalUpdateEvent event) {
    updatePortal(event.portal());
  }

  private void updatePortal(@Nullable Portal portal) {
    if (portal == null) {
      return;
    }

    val sign = signManager.get(portal);

    if (sign == null) {
      return;
    }

    sign.update();
  }
}
