package me.caneva20.wayportals.portal;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.caneva20.wayportals.utils.Region;
import me.caneva20.wayportals.utils.Vector3Int;
import org.jetbrains.annotations.Nullable;

@ToString
public class Portal extends Region {

  @Getter
  @Setter(AccessLevel.PACKAGE)
  private long id;

  public Portal(long id, Vector3Int min, Vector3Int max, @Nullable String worldName) {
    super(min, max, worldName);
    this.id = id;
  }

  public Portal(Vector3Int min, Vector3Int max, @Nullable String worldName) {
    super(min, max, worldName);
  }
}
