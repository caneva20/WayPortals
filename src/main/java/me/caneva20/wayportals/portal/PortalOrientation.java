package me.caneva20.wayportals.portal;

import me.caneva20.wayportals.utils.Vector3Int;

public class PortalOrientation {

  private final Vector3Int position;
  private final OrientationAxis axis;

  public PortalOrientation(Vector3Int position, OrientationAxis axis) {
    this.position = position;
    this.axis = axis;
  }

  public static PortalOrientation northSouth(Vector3Int position) {
    return new PortalOrientation(position, OrientationAxis.Z);
  }

  public static PortalOrientation westEast(Vector3Int position) {
    return new PortalOrientation(position, OrientationAxis.X);
  }

  public int mainAxisPos() {
    if (axis == OrientationAxis.X) {
      return position.z();
    }

    return position.x();
  }

  public int crossAxisPos() {
    if (axis == OrientationAxis.X) {
      return position.x();
    }

    return position.z();
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
