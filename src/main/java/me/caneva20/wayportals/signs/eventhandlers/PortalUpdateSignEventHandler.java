package me.caneva20.wayportals.signs.eventhandlers;

import javax.inject.Inject;
import lombok.CustomLog;
import lombok.val;
import me.caneva20.wayportals.portal.IPortalManager;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.portal.events.PortalLinkedEvent;
import me.caneva20.wayportals.portal.events.PortalUnlinkedEvent;
import me.caneva20.wayportals.portal.events.PortalUpdatedEvent;
import me.caneva20.wayportals.signs.ISignContentManager;
import me.caneva20.wayportals.signs.ISignManager;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.Nullable;

@CustomLog
public class PortalUpdateSignEventHandler extends SignEventHandler {

  private final IPortalManager portalManager;
  private final ISignManager signManager;
  private final ISignContentManager contentManager;

  @Inject
  PortalUpdateSignEventHandler(IPortalManager portalManager, ISignManager signManager, ISignContentManager contentManager) {
    this.portalManager = portalManager;
    this.signManager = signManager;
    this.contentManager = contentManager;
  }

  @EventHandler
  private void onLink(PortalLinkedEvent event) {
    updatePortal(event.sourceId());
    updatePortal(event.destinationId());
  }

  @EventHandler
  private void onUnlink(PortalUnlinkedEvent event) {
    updatePortal(event.sourceId());
    updatePortal(event.oldLinkId());
  }

  @EventHandler
  private void onUpdate(PortalUpdatedEvent event) {
    updatePortal(event.portalId());
    updatePortal(portalManager.findLink(event.portalId()));
  }

  private void updatePortal(@Nullable Long portalId) {
    updatePortal(portalManager.find(portalId));
  }

  private void updatePortal(@Nullable Portal portal) {
    if (portal == null) {
      return;
    }

    val sign = signManager.get(portal);

    if (sign == null) {
      return;
    }

    contentManager.update(sign);
  }
}
