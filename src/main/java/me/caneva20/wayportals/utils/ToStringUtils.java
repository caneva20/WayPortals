package me.caneva20.wayportals.utils;

import org.bukkit.Location;

public class ToStringUtils {

  public static String toString(Location loc) {
    var world = loc.getWorld();

    if (world != null) {
      return String
          .format("%s@%s, %s, %s", loc.getWorld().getName(), (int) loc.getX(), (int) loc.getY(),
              (int) loc.getZ());
    } else {
      return String.format("%s, %s, %s", (int) loc.getX(), (int) loc.getY(), (int) loc.getZ());
    }
  }
}
