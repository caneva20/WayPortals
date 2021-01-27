package me.caneva20.wayportals.portal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.caneva20.wayportals.utils.Vector3;
import me.caneva20.wayportals.utils.WorldVector3;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@ToString
public class Portal {

  private final long id;
  private String name = "";

  private final PortalAxis axis;
  private final WorldVector3 location;
  private final int width;
  private final int height;
  private final int depth;

  private final Vector3 size;

  @ToString.Exclude
  @Nullable
  Portal link;

  Portal(long id, WorldVector3 min, WorldVector3 max) {
    this.id = id;

    this.height = (int) (max.y() - min.y());
    this.width = (int) (max.x() - min.x());
    this.depth = (int) (max.z() - min.z());

    this.location = min;

    this.size = new Vector3(width, height, depth).add(Vector3.one);
    this.axis = min.x() == max.x() ? PortalAxis.Z : PortalAxis.X;
  }
}