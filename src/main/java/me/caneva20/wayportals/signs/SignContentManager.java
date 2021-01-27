package me.caneva20.wayportals.signs;

import lombok.val;
import me.caneva20.wayportals.portal.IPortalManager;

public class SignContentManager implements ISignContentManager {

  private final IPortalManager portalManager;

  public SignContentManager(IPortalManager portalManager) {
    this.portalManager = portalManager;
  }

  @Override
  public void update(PortalSign portalSign) {

    val portal = portalManager.find(portalSign.portalId());
    val link = portalManager.findLink(portalSign.portalId());

    var name = "(unknown)";
    var destination = "(nowhere)";

    if (portal != null) {
      name = portal.name();
    }

    if (link != null) {
      destination = link.name();
    }

    val sign = portalSign.sign();

    sign.setLine(0, String.format("§7[§6%s§7]", name));
    sign.setLine(1, String.format("§7→ §e%s", destination));
    sign.setLine(2, "");
    sign.setLine(3, "§8(right click me)");

    sign.update();
  }
}
