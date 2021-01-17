package me.caneva20.wayportals.utils;

import lombok.Getter;
import lombok.ToString;
import org.bukkit.Location;

@Getter
@ToString
public class WorldVector3 extends Vector3 {

  private final String world;

  public WorldVector3(double x, double y, double z, String world) {
    super(x, y, z);

    this.world = world;
  }

  public WorldVector3(Location location) {
    super(location.getX(), location.getY(), location.getZ());

    this.world = location.getWorld().getName();
  }
}
