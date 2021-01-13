package me.caneva20.wayportals.utils;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Vector2 {

  private final double x;
  private final double y;

  public Vector2(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Vector2(Vector3 vector3) {
    this(vector3.x(), vector3.y());
  }

  public static Vector2 min(Vector2 a, Vector2 b) {
    return new Vector2(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()));
  }

  public static Vector2 max(Vector2 a, Vector2 b) {
    return new Vector2(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()));
  }
}
