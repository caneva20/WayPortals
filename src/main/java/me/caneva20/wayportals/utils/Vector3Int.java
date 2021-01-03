package me.caneva20.wayportals.utils;

import java.io.Serializable;

public class Vector3Int implements Serializable {

  private static final long serialVersionUID = 1L;

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
    this((int) vector3.getX(), (int) vector3.getY(), (int) vector3.getZ());
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getZ() {
    return z;
  }

  @Override
  public String toString() {
    return "Vector3{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
  }

  public String toStringShort() {
    return x + "," + y + "," + z;
  }

  public Vector3 toVector3() {
    return new Vector3(x, y, z);
  }

  public static Vector3Int min(Vector3Int a, Vector3Int b) {
    return new Vector3Int(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()),
        Math.min(a.getZ(), b.getZ()));
  }

  public static Vector3Int max(Vector3Int a, Vector3Int b) {
    return new Vector3Int(Math.max(a.getX(), b.getX()), Math.max(a.getY(), b.getY()),
        Math.max(a.getZ(), b.getZ()));
  }
}
