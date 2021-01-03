package me.caneva20.wayportals.utils;

import java.io.Serializable;

public class Region implements Serializable {

  private final Vector3 from;
  private final Vector3 to;

  public Region(Vector3 from, Vector3 to) {
    this.from = from;
    this.to = to;
  }

  public Vector3 getFrom() {
    return from;
  }

  public Vector3 getTo() {
    return to;
  }

  @Override
  public String toString() {
    return "Region{" + "from=" + from + ", to=" + to + '}';
  }
}
