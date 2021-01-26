package me.caneva20.wayportals.signs.eventhandlers;

import javax.inject.Inject;
import lombok.CustomLog;
import lombok.val;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.portal.PortalManager;
import me.caneva20.wayportals.signs.SignManager;
import me.caneva20.wayportals.utils.BlockSearchUtility;
import me.caneva20.wayportals.utils.InventoryUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

@CustomLog
public class CreationSignEventHandler extends SignEventHandler {

  private final JavaPlugin plugin;
  private final PortalManager portalManager;
  private final SignManager signManager;

  @Inject
  CreationSignEventHandler(JavaPlugin plugin, PortalManager portalManager,
      SignManager signManager) {
    this.plugin = plugin;
    this.portalManager = portalManager;
    this.signManager = signManager;
  }

  @Nullable
  private Portal findPortal(Block signBlock) {
    var oppositeFace = ((WallSign) signBlock.getBlockData()).getFacing().getOppositeFace();

    var attachedBlock = signBlock.getRelative(oppositeFace);

    var neighbours = BlockSearchUtility.findNeighbours(attachedBlock, Material.NETHER_PORTAL);

    if (neighbours.size() != 1) {
      return null;
    }

    var portalBlock = neighbours.stream().findFirst().get();

    return portalManager.get(portalBlock.getLocation());
  }

  private boolean withdrawSign(Sign sign, Player player) {
    val material = SIGN_MATERIAL_MAP.get(sign.getType());

    return InventoryUtility.withdraw(player, material, 1);
  }

  private void createSignTask(Sign sign, Portal portal) {
    var block = sign.getWorld().getBlockAt(sign.getLocation());

    block.setType(sign.getType());
    block.setBlockData(sign.getBlockData());

    signManager.create(sign, portal);
  }

  private void createSign(Sign sign, Portal portal) {
    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> createSignTask(sign, portal));
  }

  @EventHandler
  private void onSignPlace(BlockPlaceEvent event) {
    var sign = getSign(event.getBlock());

    if (sign == null) {
      return;
    }

    var portal = findPortal(event.getBlock());

    if (portal == null) {
      return;
    }

    val portalSign = signManager.get(portal);

    if (portalSign != null && !(portalSign.sign().getLocation()
        .equals(event.getBlock().getLocation()))) {
      return;
    }

    event.setCancelled(true);

    if (!withdrawSign(sign, event.getPlayer())) {
      return;
    }

    createSign(sign, portal);
  }
}
