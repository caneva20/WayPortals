package me.caneva20.wayportals.portal;

import lombok.Getter;
import lombok.ToString;
import me.caneva20.wayportals.utils.Vector3;

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

  public double crossAxisPos() {
    if (axis == OrientationAxis.X) {
      return position.x();
    }

    return position.z();
  }
}
