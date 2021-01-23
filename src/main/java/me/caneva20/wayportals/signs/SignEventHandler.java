package me.caneva20.wayportals.signs;

import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import lombok.CustomLog;
import lombok.val;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.portal.PortalManager;
import me.caneva20.wayportals.portal.events.PortalLinkedEvent;
import me.caneva20.wayportals.portal.events.PortalUnlinkEvent;
import me.caneva20.wayportals.utils.BlockSearchUtility;
import me.caneva20.wayportals.utils.InventoryUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

@CustomLog
public class SignEventHandler implements Listener {

  private static final Set<Material> SIGNS = Set
      .of(Material.OAK_WALL_SIGN, Material.SPRUCE_WALL_SIGN, Material.BIRCH_WALL_SIGN,
          Material.JUNGLE_WALL_SIGN, Material.ACACIA_WALL_SIGN, Material.DARK_OAK_WALL_SIGN,
          Material.CRIMSON_WALL_SIGN, Material.WARPED_WALL_SIGN);

  private static final Map<Material, Material> signMaterialMap = Map
      .of(Material.OAK_WALL_SIGN, Material.OAK_SIGN, Material.SPRUCE_WALL_SIGN,
          Material.SPRUCE_SIGN, Material.BIRCH_WALL_SIGN, Material.BIRCH_SIGN,
          Material.JUNGLE_WALL_SIGN, Material.JUNGLE_SIGN, Material.ACACIA_WALL_SIGN,
          Material.ACACIA_SIGN, Material.DARK_OAK_WALL_SIGN, Material.DARK_OAK_SIGN,
          Material.CRIMSON_WALL_SIGN, Material.CRIMSON_SIGN, Material.WARPED_WALL_SIGN,
          Material.WARPED_SIGN);

  private final JavaPlugin plugin;
  private final PortalManager portalManager;
  private final SignManager signManager;

  @Inject
  SignEventHandler(JavaPlugin plugin, PortalManager portalManager, SignManager signManager) {
    this.plugin = plugin;
    this.portalManager = portalManager;
    this.signManager = signManager;
  }

  private Sign findSign(Block signBlock) {
    return (Sign) signBlock.getState();
  }

  private @Nullable Portal findPortal(Block signBlock) {
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
    val material = signMaterialMap.get(sign.getType());

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
    if (!SIGNS.contains(event.getBlock().getType())) {
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
    var sign = findSign(event.getBlock());

    if (!withdrawSign(sign, event.getPlayer())) {
      return;
    }

    createSign(sign, portal);
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
