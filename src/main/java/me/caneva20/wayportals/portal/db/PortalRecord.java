package me.caneva20.wayportals.portal.db;


import lombok.Data;
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

  private final @Nullable Integer linkedPortalId;
}
