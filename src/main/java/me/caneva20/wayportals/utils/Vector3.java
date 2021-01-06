package me.caneva20.wayportals.utils;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Vector3 {

  private final double x;
  private final double y;
  private final double z;

  public Vector3(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vector3(Vector3Int vector3Int) {
    this(vector3Int.x(), vector3Int.y(), vector3Int.z());
  }

  public Vector3Int toVector3Int() {
    return new Vector3Int((int) x, (int) y, (int) z);
  }

  public static Vector3 min(Vector3 a, Vector3 b) {
    return new Vector3(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()), Math.min(a.z(), b.z()));
  }

  public static Vector3 max(Vector3 a, Vector3 b) {
    return new Vector3(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()), Math.max(a.z(), b.z()));
  }
}
