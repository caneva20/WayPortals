package me.caneva20.wayportals.portal;

import lombok.Getter;
import lombok.ToString;
import me.caneva20.wayportals.utils.MathUtils;
import me.caneva20.wayportals.utils.Vector2;

@Getter
@ToString
public class PortalDimensions {

  private final Vector2 min;
  private final Vector2 max;

  private final double width;
  private final double height;

  public PortalDimensions(Vector2 min, Vector2 max) {
    this.min = min;
    this.max = max;

    width = max.x() - min.x();
    height = max.y() - min.y();
  }

  public Vector2 map(Vector2 value, PortalDimensions target) {
    final var P = .3; //Width factor. Half the player width (1.16)
    final var Q = .8; //Height factor. Player height (1.16)

    var x = MathUtils
        .map(value.x(), min.x() + P, max.x() - P + 1, target.min.x() + P, target.max.x() - P + 1);

    var y = MathUtils.map(value.y(), min.y(), max.y() - Q, target.min.y(), target.max.y() - Q);

    return new Vector2(x, y);
  }
}
