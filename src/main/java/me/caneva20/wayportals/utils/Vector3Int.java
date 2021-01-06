package me.caneva20.wayportals.utils;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Vector3Int {

  private final int x;
  private final int y;
  private final int z;

  public Vector3Int(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vector3Int(double x, double y, double z) {
    this.x = (int) x;
    this.y = (int) y;
    this.z = (int) z;
  }

  public Vector3Int(Vector3 vector3) {
    this((int) vector3.x(), (int) vector3.y(), (int) vector3.z());
  }

  public Vector3 toVector3() {
    return new Vector3(x, y, z);
  }

  public static Vector3Int min(Vector3Int a, Vector3Int b) {
    return new Vector3Int(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()), Math.min(a.z(), b.z()));
  }

  public static Vector3Int max(Vector3Int a, Vector3Int b) {
    return new Vector3Int(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()), Math.max(a.z(), b.z()));
  }
}
