package me.caneva20.wayportals.signs.eventhandlers;

import javax.inject.Inject;
import me.caneva20.wayportals.portal.IPortalManager;
import me.caneva20.wayportals.signs.ISignManager;
import me.caneva20.wayportals.utils.SignMenu;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractionSignEventHandler extends SignEventHandler {

  private final SignMenu signMenu;
  private final ISignManager signManager;
  private final IPortalManager portalManager;

  @Inject
  InteractionSignEventHandler(SignMenu signMenu, ISignManager manager,
      IPortalManager portalManager) {
    this.signMenu = signMenu;
    this.signManager = manager;
    this.portalManager = portalManager;
  }

  @EventHandler(ignoreCancelled = true)
  private void onInteract(PlayerInteractEvent event) {
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }

    var block = event.getClickedBlock();

    if (block == null) {
      return;
    }

    var sign = getSign(block);

    if (sign == null) {
      return;
    }

    var portalSign = signManager.get(sign);

    if (portalSign == null) {
      return;
    }

    var portal = portalSign.portal();

    if (portal == null) {
      return;
    }

    //TODO: Move string to a lang file
    signMenu.getInput(event.getPlayer(), "Type portal name", portal.name(), Material.OAK_SIGN,
        (p, input) -> {
          portal.name(input);
          portalManager.update(portal);

          return true;
        });
  }
}
