package me.caneva20.wayportals.signs;

import lombok.Getter;
import me.caneva20.wayportals.utils.WorldVector3;
import org.bukkit.block.Sign;
import org.jetbrains.annotations.Nullable;

@Getter
public class PortalSign {

  private final Long portalId;

  private final long id;

  private final String world;
  private final int x;
  private final int y;
  private final int z;

  private final Sign sign;

  PortalSign(int id, WorldVector3 location, Sign sign, @Nullable Long portalId) {
    this.portalId = portalId;
    this.sign = sign;
    this.id = id;

    world = location.world();
    x = (int) location.x();
    y = (int) location.y();
    z = (int) location.z();
  }
}
