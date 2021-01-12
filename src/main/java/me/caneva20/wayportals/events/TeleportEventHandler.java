package me.caneva20.wayportals.events;

import java.util.Objects;
import javax.inject.Inject;
import me.caneva20.paperlib.PaperLib;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.portal.PortalUtility;
import me.caneva20.wayportals.utils.MathUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public class TeleportEventHandler implements Listener {

  private static final BlockFace[] faces = {BlockFace.SELF, BlockFace.NORTH, BlockFace.SOUTH,
      BlockFace.EAST, BlockFace.WEST};

  private final JavaPlugin plugin;

  @Inject
  TeleportEventHandler(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  private @Nullable Portal findPortal(Block block) {
    Block portalBlock = null;

    for (BlockFace face : faces) {
      portalBlock = block.getRelative(face);

      if (portalBlock.getType() == Material.NETHER_PORTAL) {
        break;
      }

      portalBlock = null;
    }

    if (portalBlock == null) {
      return null;
    }

    return PortalUtility.find(portalBlock);
  }

  private Location getDestination(Portal portal, Location from) {
    var link = portal.link();
    final var P = .3; //Width factor
    final var Q = .8; //Height factor

    var x = MathUtils
        .map(from.getX(), portal.from().x() + P, portal.to().x() - P + 1, link.from().x() + P,
            link.to().x() - P + 1);

    var y = MathUtils.map(from.getY(), portal.from().y(), portal.to().y() - Q, link.from().y(),
        link.to().y() - Q);

    var z = MathUtils
        .map(from.getZ(), portal.from().z() + P, portal.to().z() - P + 1, link.from().z() + P,
            link.to().z() - P + 1);

    var world = plugin.getServer().getWorld(Objects.requireNonNull(link.worldName()));

    var yaw = portal.orientation().getYawTo(link.orientation());

    return new Location(world, x, y, z, from.getYaw() + yaw, from.getPitch());
  }

  @EventHandler
  private void onPortalTeleport(PlayerPortalEvent event) {
    if (event.getCause() != TeleportCause.NETHER_PORTAL) {
      return;
    }

    var player = event.getPlayer();
    var portal = findPortal(event.getFrom().getBlock());

    if (portal == null || !portal.hasLink()) {
      return;
    }

    event.setCancelled(true);
    var destination = getDestination(portal, event.getFrom());
    PaperLib.teleportAsync(player, destination, TeleportCause.PLUGIN);
  }
}
