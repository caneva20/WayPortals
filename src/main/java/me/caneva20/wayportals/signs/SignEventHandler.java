package me.caneva20.wayportals.signs;

import java.util.Set;
import javax.inject.Inject;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.caneva20.messagedispatcher.dispachers.IMessageDispatcher;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.portal.PortalManager;
import me.caneva20.wayportals.portal.events.PortalLinkedEvent;
import me.caneva20.wayportals.portal.events.PortalUnlinkEvent;
import me.caneva20.wayportals.utils.BlockSearchUtility;
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
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class SignEventHandler implements Listener {

  private static final Set<Material> SIGNS = Set
      .of(Material.OAK_WALL_SIGN, Material.SPRUCE_WALL_SIGN, Material.BIRCH_WALL_SIGN,
          Material.JUNGLE_WALL_SIGN, Material.ACACIA_WALL_SIGN, Material.DARK_OAK_WALL_SIGN,
          Material.CRIMSON_WALL_SIGN, Material.WARPED_WALL_SIGN);

  private final JavaPlugin plugin;
  private final IMessageDispatcher dispatcher;
  private final PortalManager portalManager;
  private final SignManager signManager;

  private Sign findSign(Block signBlock) {
    return (Sign) signBlock.getState();
  }

  private @Nullable Portal findPortal(Block signBlock, Player player) {
    var oppositeFace = ((WallSign) signBlock.getBlockData()).getFacing().getOppositeFace();

    var attachedBlock = signBlock.getRelative(oppositeFace);

    var neighbours = BlockSearchUtility.findNeighbours(attachedBlock, Material.NETHER_PORTAL);

    if (neighbours.size() != 1) {
      if (neighbours.size() > 1) {
        dispatcher.debug(player, "More than one portal found!");
      } else {
        dispatcher.debug(player, "No portal found");
      }

      return null;
    }

    var portalBlock = neighbours.stream().findFirst().get();

    return portalManager.get(portalBlock.getLocation());
  }

  private void createSign(Sign sign, Portal portal, Player player) {
    var block = sign.getWorld().getBlockAt(sign.getLocation());

    block.setType(sign.getType());
    block.setBlockData(sign.getBlockData());

    signManager.create(sign, portal);

    dispatcher.debug(player, "Sign created!");
  }

  @EventHandler
  private void onSignPlace(BlockPlaceEvent event) {
    if (!SIGNS.contains(event.getBlock().getType())) {
      return;
    }

    var player = event.getPlayer();
    var portal = findPortal(event.getBlock(), player);

    if (portal != null) {
      if (signManager.get(portal) != null) {
        log.info("This portal already have a sign assigned");

        return;
      }

      event.setCancelled(true);
      var signBlock = findSign(event.getBlock());

      plugin.getServer().getScheduler()
          .scheduleSyncDelayedTask(plugin, () -> createSign(signBlock, portal, player));
    }
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
