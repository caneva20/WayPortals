package me.caneva20.wayportals.events;

import java.util.Objects;
import javax.inject.Inject;
import me.caneva20.paperlib.PaperLib;
import me.caneva20.wayportals.portal.OrientationAxis;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.portal.PortalUtility;
import me.caneva20.wayportals.utils.Vector2;
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

    var fromPos = new Vector2(portal.orientation().crossAxisPos(from), from.getY());

    var target = portal.dimensions().map(fromPos, link.dimensions());
    var yaw = portal.orientation().getYawTo(link.orientation());
    var world = plugin.getServer().getWorld(Objects.requireNonNull(link.worldName()));

    if (link.orientation().axis() == OrientationAxis.Z) {
      return new Location(world, link.orientation().mainAxisPos(link), target.y(), target.x(),
          from.getYaw() + yaw, from.getPitch());
    }

    return new Location(world, target.x(), target.y(), link.orientation().mainAxisPos(link),
        from.getYaw() + yaw, from.getPitch());
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
