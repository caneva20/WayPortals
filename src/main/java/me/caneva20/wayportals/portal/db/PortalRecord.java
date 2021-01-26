package me.caneva20.wayportals.portal.db;


import lombok.Data;
import me.caneva20.wayportals.utils.WorldVector3;
import org.jetbrains.annotations.Nullable;

@Data
public class PortalRecord {

  private final long id;

  private final String world;
  private final int minX;
  private final int minY;
  private final int minZ;
  private final int maxX;
  private final int maxY;
  private final int maxZ;

  @Nullable
  private final Long linkedPortalId;

  public WorldVector3 location() {
    return new WorldVector3(minX, minY, minZ, world);
  }
}
