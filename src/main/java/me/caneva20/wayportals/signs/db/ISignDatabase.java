package me.caneva20.wayportals.signs.db;

import me.caneva20.wayportals.utils.WorldVector3;
import org.jetbrains.annotations.Nullable;

public interface ISignDatabase {

  void initialize();

  @Nullable SignRecord find(long id);

  @Nullable SignRecord find(WorldVector3 location);

  @Nullable SignRecord create(WorldVector3 location, int portalId);

  void delete(long signId);
}
