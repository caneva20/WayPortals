package me.caneva20.wayportals.portalbinder.eventhandlers;

import java.util.Random;
import javax.inject.Inject;
import lombok.val;
import me.caneva20.wayportals.portalbinder.IPortalBinderManager;
import me.caneva20.wayportals.portalbinder.config.IBinderConfig;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class DropEventHandler implements Listener {

  private final IBinderConfig config;
  private final IPortalBinderManager manager;

  private final Random rand = new Random();

  @Inject
  public DropEventHandler(IBinderConfig config, IPortalBinderManager manager) {
    this.config = config;
    this.manager = manager;
  }

  private boolean hasValidMaterial(Block block) {
    return config.dropMaterials().containsKey(block.getType());
  }

  @EventHandler(ignoreCancelled = true)
  private void onBlockBreak(BlockBreakEvent event) {
    val player = event.getPlayer();

    if (player.getGameMode() == GameMode.CREATIVE) {
      return;
    }

    var hasSilktouch = player.getInventory().getItemInMainHand()
        .containsEnchantment(Enchantment.SILK_TOUCH);

    if (hasSilktouch && !config.dropWithSilkTouch()) {
      return;
    }

    val block = event.getBlock();
    if (!hasValidMaterial(block)) {
      return;
    }

    val dropChance = config.dropMaterials().get(block.getType());

    if (rand.nextDouble() <= dropChance) {
      val location = block.getLocation();
      val stack = manager.create().stack();
      val world = location.getWorld();

      if (world == null) {
        return;
      }

      world.dropItemNaturally(location, stack);
    }
  }
}
