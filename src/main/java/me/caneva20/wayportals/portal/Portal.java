package me.caneva20.wayportals.portal;

import java.util.Objects;
import lombok.Getter;
import lombok.ToString;
import me.caneva20.wayportals.utils.Vector3;
import me.caneva20.wayportals.utils.WorldVector3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

@Getter
@ToString
public class Portal {

  private final long id;

  private final PortalAxis axis;
  private final WorldVector3 location;
  private final int width;
  private final int height;
  private final int depth;

  private final Vector3 size;

  @ToString.Exclude
  @Nullable Portal link;

  Portal(long id, WorldVector3 min, WorldVector3 max) {
    this.id = id;

    this.height = (int) (max.y() - min.y());
    this.width = (int) (max.x() - min.x());
    this.depth = (int) (max.z() - min.z());

    this.location = min;

    this.size = new Vector3(width, height, depth).add(Vector3.one);
    this.axis = min.x() == max.x() ? PortalAxis.Z : PortalAxis.X;
  }

  public boolean hasLink() {
    return link != null;
  }

  @Nullable
  public Location getDestination(Vector3 playerLocation) {
    if (link == null) {
      return null;
    }

    var relativePos = playerLocation.subtract(location);

    if (inverse()) {
      relativePos = size.subtract(relativePos.x(), size.y() - relativePos.y(), relativePos.z());
    }

    var pos = relativePos.divide(size);

    if (axis != link.axis) {
      pos = new Vector3(pos.z(), pos.y(), pos.x());
    }

    pos = pos.multiply(link.size).add(link.location);

    var world = Bukkit.getServer().getWorld(link.location().world());

    return new Location(world, pos.x(), pos.y(), pos.z());
  }

  public float getLinkYaw() {
    if (link == null) {
      return 0;
    }

    if (axis == link.axis) {
      return 0;
    }

    var dirX = location.x() > link.location.x() ? 1 : -1;
    var dirZ = location.z() > link.location.z() ? -1 : 1;
    var axisF = axis == PortalAxis.X ? 1 : -1;

    return 90 * dirZ * dirX * axisF;
  }

  private boolean inverse() {
    Objects.requireNonNull(link);

    if (axis != link.axis) {
      var a = location.x() > link.location.x();
      var b = location.z() > link.location.z();

      return a == b;
    }

    return false;
  }
}