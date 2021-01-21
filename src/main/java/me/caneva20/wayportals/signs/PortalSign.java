package me.caneva20.wayportals.signs;

import lombok.Getter;
import lombok.ToString;
import lombok.val;
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

    update();
  }

  public void update() {
    val link = portal.link();

    if (link != null) {
      sign.setLine(0, String.format("%s -> %s", portal.id(), link.id()));
      sign.setLine(1, "");
    } else {
      sign.setLine(0, portal.id() + "");
      sign.setLine(1, "Not linked");
    }

    sign.setLine(2, portal.location().world());

    sign.setLine(3, String
        .format("%s, %s, %s", (int) portal.location().x(), (int) portal.location().y(),
            (int) portal.location().z()));

    sign.update();
  }
}
