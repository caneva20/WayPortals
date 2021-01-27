package me.caneva20.wayportals.portal.db;


import lombok.AllArgsConstructor;
import lombok.Data;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.utils.WorldVector3;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class PortalRecord {

  private final long id;
  private final String name;

  private final String world;
  private final int minX;
  private final int minY;
  private final int minZ;
  private final int maxX;
  private final int maxY;
  private final int maxZ;

  @Nullable
  private final Long linkedPortalId;

  public PortalRecord(Portal portal) {
    id = portal.id();

    name = portal.name();
    world = portal.location().world();
    minX = (int) portal.location().x();
    minY = (int) portal.location().y();
    minZ = (int) portal.location().z();
    maxX = (int) portal.location().x() + portal.width();
    maxY = (int) portal.location().y() + portal.height();
    maxZ = (int) portal.location().z() + portal.depth();

    if (portal.linkId() != null) {
      linkedPortalId = portal.linkId();
    } else {
      linkedPortalId = null;
    }
  }

  public WorldVector3 location() {
    return new WorldVector3(minX, minY, minZ, world);
  }
}
