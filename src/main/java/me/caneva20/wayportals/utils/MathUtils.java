package me.caneva20.wayportals.utils;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MathUtils {

  public static double map(double value, double fMin, double fMax, double tMin, double tMax) {
    if (tMin == tMax) {
      return tMin;
    }

    if (fMin == fMax) {
      return (tMin + tMax) / 2;
    }

    return (tMax - tMin) * (max(0, min(1, (value - fMin) / (fMax - fMin)))) + tMin;
  }
}
