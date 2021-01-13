package me.caneva20.wayportals.portal;

import lombok.Getter;
import lombok.ToString;
import me.caneva20.wayportals.utils.Region;
import me.caneva20.wayportals.utils.Vector3;
import org.bukkit.Location;

@Getter
@ToString
public class PortalOrientation {

  private final Vector3 position;
  private final OrientationAxis axis;

  public PortalOrientation(Vector3 position, OrientationAxis axis) {
    this.position = position;
    this.axis = axis;
  }

  public static PortalOrientation northSouth(Vector3 position) {
    return new PortalOrientation(position, OrientationAxis.Z);
  }

  public static PortalOrientation westEast(Vector3 position) {
    return new PortalOrientation(position, OrientationAxis.X);
  }

  public double mainAxisPos() {
    if (axis == OrientationAxis.X) {
      return position.z();
    }

    return position.x();
  }

  public double mainAxisPos(Location location) {
    if (axis == OrientationAxis.X) {
      return location.getZ();
    }

    return location.getX();
  }

  public double mainAxisPos(Region region) {
    if (axis == OrientationAxis.X) {
      return region.from().z();
    }

    return region.from().x();
  }

  public double crossAxisPos() {
    if (axis == OrientationAxis.X) {
      return position.x();
    }

    return position.z();
  }

  public double crossAxisPos(Location location) {
    if (axis == OrientationAxis.X) {
      return location.getX();
    }

    return location.getZ();
  }

  public double crossAxisPos(Region region) {
    if (axis == OrientationAxis.X) {
      return region.from().x();
    }

    return region.from().z();
  }

  public float getYawTo(PortalOrientation other) {
    if (axis == other.axis) {
      return 0;
    }

    var dirF = mainAxisPos() > other.crossAxisPos() ? 1 : -1;
    var axisF = axis == OrientationAxis.X ? 1 : -1;
    var dir = crossAxisPos() > other.mainAxisPos() ? -1 : 1;

    return 90 * dir * dirF * axisF;
  }
}
