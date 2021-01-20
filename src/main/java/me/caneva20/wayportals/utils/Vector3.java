package me.caneva20.wayportals.utils;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Vector3 {

  /**
   * A Vector3 with each of its coordinates equal to 0
   */
  public static final Vector3 zero = new Vector3(0, 0, 0);

  /**
   * A Vector3 with each of its coordinates equal to 1
   */
  public static final Vector3 one = new Vector3(1, 1, 1);


  /**
   * The x coordinate
   */
  private final double x;

  /**
   * The y coordinate
   */

  private final double y;
  /**
   * The z coordinate
   */
  private final double z;

  public Vector3(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }


  /**
   * Adds together each element of this vector with [other]'s equivalent.
   *
   * @param other the vector to add this
   * @return this vector added to [other]
   */
  public Vector3 add(Vector3 other) {
    return new Vector3(x + other.x, y + other.y, z + other.z);
  }

  /**
   * Subtracts each element of this vector from [other]'s equivalent.
   *
   * @param other the vector to subtract from this
   * @return this vector subtracted from [other]
   */
  public Vector3 subtract(Vector3 other) {
    return new Vector3(x - other.x, y - other.y, z - other.z);
  }

  /**
   * Subtracts each element of this vector from the equivalent value.
   *
   * @param x the x value
   * @param y the y value
   * @param z the z value
   * @return this vector subtracted from [other]
   */
  public Vector3 subtract(final double x, final double y, final double z) {
    return new Vector3(this.x - x, this.y - y, this.z - z);
  }

  /**
   * Divides each element of this vector by [other]'s equivalent.
   *
   * @param other the vector to divide this with
   * @return this vector divided by [other]
   */
  public Vector3 divide(Vector3 other) {
    return new Vector3(x / other.x, y / other.y, z / other.z);
  }

  /**
   * Divides each element of this vector by [scalar]
   *
   * @param scalar the value to divide each of this' elements by
   * @return this vector divided by [scalar]
   */
  public Vector3 divide(double scalar) {
    return new Vector3(x / scalar, y / scalar, z / scalar);
  }

  /**
   * Multiplies each element of this vector by [other]'s equivalent.
   *
   * @param other the vector to multiply this with
   * @return this vector multiplied by [other]
   */
  public Vector3 multiply(Vector3 other) {
    return new Vector3(x * other.x, y * other.y, z * other.z);
  }

  /**
   * Multiplies each element of this vector by [scalar]
   *
   * @param scalar the value to multiply each of this' elements by
   * @return this vector multiplied by [scalar]
   */
  public Vector3 multiply(double scalar) {
    return new Vector3(x * scalar, y * scalar, z * scalar);
  }


  /**
   * Constructs a Vector3 from a Vector3Int
   *
   * @param vector3Int The Vector3Int to construct from
   */
  public Vector3(Vector3Int vector3Int) {
    this(vector3Int.x(), vector3Int.y(), vector3Int.z());
  }

  /**
   * Converts a Vector3 into a Vector3Int. The values are floored
   *
   * @return This vector as a Vector3Int
   */
  public Vector3Int toVector3Int() {
    return new Vector3Int((int) x, (int) y, (int) z);
  }

  /**
   * @param a The first vector
   * @param b The second vector
   * @return A new vector with smallest values from each coordinate
   */
  public static Vector3 min(Vector3 a, Vector3 b) {
    return new Vector3(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()), Math.min(a.z(), b.z()));
  }

  /**
   * @param a The first vector
   * @param b The second vector
   * @return A new vector with biggest values from each coordinate
   */
  public static Vector3 max(Vector3 a, Vector3 b) {
    return new Vector3(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()), Math.max(a.z(), b.z()));
  }
}
