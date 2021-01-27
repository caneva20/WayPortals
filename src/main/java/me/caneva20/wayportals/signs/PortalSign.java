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
    var destination = "(nowhere)";

    if (link != null) {
      destination = link.name();
    }

    sign.setLine(0, String.format("§7[§6%s§7]", portal.name()));
    sign.setLine(1, String.format("§7→ §e%s", destination));
    sign.setLine(2, "");
    sign.setLine(3, "§8(right click me)");

    sign.update();
  }
}
