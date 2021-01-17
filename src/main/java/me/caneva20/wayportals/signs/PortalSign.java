package me.caneva20.wayportals.signs;

import lombok.Getter;
import lombok.ToString;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.utils.WorldVector3;
import org.bukkit.block.Sign;
import org.jetbrains.annotations.Nullable;

@Getter
public class PortalSign {

  @ToString.Exclude
  private final Portal portal;

  private final long id;

  private final String world;
  private final int x;
  private final int y;
  private final int z;

  private final Sign sign;

  PortalSign(int id, WorldVector3 location, Sign sign, @Nullable Portal portal) {
    this.portal = portal;
    this.sign = sign;
    this.id = id;

    world = location.world();
    x = (int) location.x();
    y = (int) location.y();
    z = (int) location.z();

    updateSign();
  }

  private void updateSign() {
    sign.setLine(0, String.format("ID: %s", portal.id()));

    if (portal.hasLink()) {
      sign.setLine(1, String.format("Link ID: %s", portal.link().id()));
    } else {
      sign.setLine(1, "Not linked");
    }

    sign.update();
  }
}
