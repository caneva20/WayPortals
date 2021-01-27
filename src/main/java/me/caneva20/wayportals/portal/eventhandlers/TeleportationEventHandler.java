package me.caneva20.wayportals.portal.eventhandlers;

import io.papermc.lib.PaperLib;
import javax.inject.Inject;
import lombok.val;
import me.caneva20.wayportals.portal.IPortalManager;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.teleport.ITeleportManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.jetbrains.annotations.Nullable;

public class TeleportationEventHandler implements Listener {

  private static final BlockFace[] faces = {BlockFace.SELF, BlockFace.NORTH, BlockFace.SOUTH,
      BlockFace.EAST, BlockFace.WEST};

  private final IPortalManager portalManager;
  private final ITeleportManager teleportManager;

  @Inject
  TeleportationEventHandler(IPortalManager portalManager, ITeleportManager teleportManager) {
    this.portalManager = portalManager;
    this.teleportManager = teleportManager;
  }

  @Nullable
  private Portal findPortal(Block block) {
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

    return portalManager.get(portalBlock.getLocation());
  }

  private boolean hasValidLink(Portal portal) {
    var linkId = portal.linkId();

    if (linkId == null) {
      return false;
    }

    var link = portalManager.find(linkId);

    if (link == null) {
      return false;
    }

    var world = Bukkit.getServer().getWorld(link.location().world());

    if (world == null) {
      portalManager.delete(link);
      return false;
    }

    var from = link.location();

    val location = new Location(world, from.x(), from.y(), from.z());

    var destPortal = portalManager.get(location);

    if (destPortal == null || destPortal.id() != link.id()) {
      portalManager.delete(link);

      return false;
    }

    return true;
  }

  @EventHandler
  private void onPortalTeleport(PlayerPortalEvent event) {
    if (event.getCause() != TeleportCause.NETHER_PORTAL) {
      return;
    }

    var player = event.getPlayer();
    var portal = findPortal(event.getFrom().getBlock());

    if (portal == null || !hasValidLink(portal)) {
      return;
    }

    event.setCancelled(true);
    var destination = teleportManager.getDestination(portal, event.getFrom());

    if (destination != null) {
      PaperLib.teleportAsync(player, destination, TeleportCause.PLUGIN);
    }
  }
}
