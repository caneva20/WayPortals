package me.caneva20.wayportals.utils.data;

import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.nio.ByteBuffer;
import java.util.Objects;

public class PersistentLocation implements PersistentDataType<byte[], Location> {

  private final JavaPlugin plugin;

  @Inject
  PersistentLocation(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @NotNull
  @Override
  public Class<byte[]> getPrimitiveType() {
    return byte[].class;
  }

  @NotNull
  @Override
  public Class<Location> getComplexType() {
    return Location.class;
  }

  @Override
  public byte @NotNull [] toPrimitive(@NotNull Location complex,
      @NotNull PersistentDataAdapterContext context) {
    var worldNameBytes = Objects.requireNonNull(complex.getWorld()).getName().getBytes();

    //28 bytes = 1 int(4) and 3 doubles (3 * 8)
    var buffer = ByteBuffer.allocate(worldNameBytes.length + 28);

    buffer.putInt(worldNameBytes.length);
    buffer.put(worldNameBytes);

    buffer.putDouble(complex.getX());
    buffer.putDouble(complex.getY());
    buffer.putDouble(complex.getZ());

    return buffer.array();
  }

  @NotNull
  @Override
  public Location fromPrimitive(byte @NotNull [] primitive,
      @NotNull PersistentDataAdapterContext context) {
    var buffer = ByteBuffer.wrap(primitive);

    try {
      var worldNameSize = buffer.getInt();
      var worldNameBytes = new byte[worldNameSize];

      buffer.get(worldNameBytes);

      var worldName = new String(worldNameBytes);
      var world = plugin.getServer().getWorld(worldName);

      return new Location(world, buffer.getDouble(), buffer.getDouble(), buffer.getDouble());
    } catch (Exception ex) {
      ex.printStackTrace();

      return new Location(null, 0, 0, 0);
    }
  }
}
