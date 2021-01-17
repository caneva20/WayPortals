package me.caneva20.wayportals.portal;

import lombok.Getter;
import lombok.ToString;
import me.caneva20.wayportals.utils.Region;
import me.caneva20.wayportals.utils.Vector3Int;

@ToString
@Getter
public class Portal extends Region {

  private final long id;
  private final PortalOrientation orientation;
  private final PortalDimensions dimensions;

  @ToString.Exclude
  Portal link;

  Portal(long id, PortalOrientation orientation, PortalDimensions dimensions) {
    //TODO: Remove region
    super(new Vector3Int(0, 0, 0), new Vector3Int(0, 0, 0), "");

    this.id = id;
    this.orientation = orientation;
    this.dimensions = dimensions;
  }

  public boolean hasLink() {
    return link != null;
  }

  public PortalDimensions dimensions(boolean inverse) {
    if (inverse) {
      return dimensions().inverse();
    } else {
      return dimensions();
    }
  }
}