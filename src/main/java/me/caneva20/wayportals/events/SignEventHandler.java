package me.caneva20.wayportals.events;

import java.util.Set;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.caneva20.messagedispatcher.dispachers.IMessageDispatcher;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.portal.PortalManager;
import me.caneva20.wayportals.signs.SignManager;
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
      event.setCancelled(true);
      var signBlock = findSign(event.getBlock());

      plugin.getServer().getScheduler()
          .scheduleSyncDelayedTask(plugin, () -> createSign(signBlock, portal, player));
    }
  }
}
