package me.caneva20.wayportals.teleport;

import javax.inject.Inject;
import javax.inject.Singleton;
import me.caneva20.wayportals.portal.IPortalManager;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.portal.PortalAxis;
import me.caneva20.wayportals.utils.Vector3;
import me.caneva20.wayportals.utils.WorldVector3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class TeleportManager implements ITeleportManager {

  private final IPortalManager portalManager;

  @Inject
  public TeleportManager(IPortalManager portalManager) {
    this.portalManager = portalManager;
  }

  @Override
  @Nullable
  public Location getDestination(@NotNull Portal portal, @NotNull Location playerPosition) {
    final var playerVPos = new WorldVector3(playerPosition);

    var linkId = portal.linkId();

    if (linkId == null) {
      return null;
    }

    var link = portalManager.find(linkId);

    if (link == null) {
      return null;
    }

    var relativePos = playerVPos.subtract(portal.location());

    if (inverse(portal, link)) {
      relativePos = portal.size()
          .subtract(relativePos.x(), portal.size().y() - relativePos.y(), relativePos.z());
    }

    var pos = relativePos.divide(portal.size());

    if (portal.axis() != link.axis()) {
      pos = new Vector3(pos.z(), pos.y(), pos.x());
    }

    pos = pos.multiply(link.size()).add(link.location());

    var world = Bukkit.getServer().getWorld(link.location().world());

    return new Location(world, pos.x(), pos.y(), pos.z(),
        playerPosition.getYaw() + getLinkYaw(portal, link), playerPosition.getPitch());
  }

  private float getLinkYaw(@NotNull Portal portal, @Nullable Portal link) {
    if (link == null) {
      return 0;
    }

    if (portal.axis() == link.axis()) {
      return 0;
    }

    var dirX = portal.location().x() > link.location().x() ? 1 : -1;
    var dirZ = portal.location().z() > link.location().z() ? -1 : 1;
    var axisF = portal.axis() == PortalAxis.X ? 1 : -1;

    return 90 * dirZ * dirX * axisF;
  }

  private boolean inverse(@NotNull Portal portal, @NotNull Portal link) {
    if (portal.axis() != link.axis()) {
      var a = portal.location().x() > link.location().x();
      var b = portal.location().z() > link.location().z();

      return a == b;
    }

    return false;
  }
}
