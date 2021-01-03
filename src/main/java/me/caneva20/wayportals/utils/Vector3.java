package me.caneva20.wayportals.utils;

import java.io.Serializable;

public class Vector3 implements Serializable {

  private final double x;
  private final double y;
  private final double z;

  public Vector3(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getZ() {
    return z;
  }

  @Override
  public String toString() {
    return "Vector3{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
  }

  public static Vector3 min(Vector3 a, Vector3 b) {
    return new Vector3(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()),
        Math.min(a.getZ(), b.getZ()));
  }

  public static Vector3 max(Vector3 a, Vector3 b) {
    return new Vector3(Math.max(a.getX(), b.getX()), Math.max(a.getY(), b.getY()),
        Math.max(a.getZ(), b.getZ()));
  }
}
