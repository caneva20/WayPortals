package me.caneva20.wayportals.signs.db;

import lombok.Data;
import me.caneva20.wayportals.utils.WorldVector3;

@Data
public class SignRecord {

  private final int id;
  private final int portalId;

  private final String world;
  private final int x;
  private final int y;
  private final int z;

  public WorldVector3 location() {
    return new WorldVector3(x, y, z, world);
  }
}
