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
  private final PortalOrientation orientation;
  private final WorldVector3 location;
  private final int width;
  private final int height;
  private final int depth;

  private final Vector3 size;

  @ToString.Exclude
  @Nullable Portal link;

  Portal(long id, WorldVector3 min, WorldVector3 max, PortalOrientation orientation) {
    this.id = id;
    this.orientation = orientation;

    this.height = (int) (max.y() - min.y());
    this.width = (int) (max.x() - min.x());
    this.depth = (int) (max.z() - min.z());

    this.location = min;

    this.size = new Vector3(width, height, depth).add(Vector3.one);
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

    if (orientation.axis() != link.orientation.axis()) {
      pos = new Vector3(pos.z(), pos.y(), pos.x());
    }

    pos = pos.multiply(link.size).add(link.location);

    var world = Bukkit.getServer().getWorld(link.location().world());

    return new Location(world, pos.x(), pos.y(), pos.z());
  }

  private boolean inverse() {
    Objects.requireNonNull(link);

    if (orientation().axis() != link.orientation().axis()) {
      var a = orientation().mainAxisPos() > link.orientation().crossAxisPos();
      var b = orientation().crossAxisPos() > link.orientation().mainAxisPos();

      return a == b;
    }

    return false;
  }
}