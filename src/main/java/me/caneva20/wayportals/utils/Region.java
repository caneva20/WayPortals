package me.caneva20.wayportals.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@ToString
public class Region {

  private final Vector3Int from;
  private final Vector3Int to;

  @Nullable
  @Setter(AccessLevel.NONE)
  private final String worldName;

  public Region(Vector3Int from, Vector3Int to, @Nullable String worldName) {
    this.from = from;
    this.to = to;
    this.worldName = worldName;
  }

  public Region(Vector3Int from, Vector3Int to) {
    this(from, to, null);
  }
}
