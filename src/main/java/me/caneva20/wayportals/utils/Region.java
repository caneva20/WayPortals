package me.caneva20.wayportals.utils;

import org.jetbrains.annotations.Nullable;

public class Region {

  private final Vector3Int from;
  private final Vector3Int to;

  @Nullable
  private final String worldName;

  public Region(Vector3Int from, Vector3Int to, @Nullable String worldName) {
    this.from = from;
    this.to = to;
    this.worldName = worldName;
  }

  public Region(Vector3Int from, Vector3Int to) {
    this(from, to, null);
  }

  public Vector3Int getFrom() {
    return from;
  }

  public Vector3Int getTo() {
    return to;
  }

  public @Nullable String getWorldName() {
    return worldName;
  }

  @Override
  public String toString() {
    return "Region{" + "from=" + from + ", to=" + to + '}';
  }

  public String toStringShort() {
    if (worldName != null) {
      return "[" + from.toStringShort() + "],[" + to.toStringShort() + "]@" + worldName;
    }

    return "[" + from.toStringShort() + "],[" + to.toStringShort() + "]";
  }
}
