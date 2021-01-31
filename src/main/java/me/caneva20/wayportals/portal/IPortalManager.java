package me.caneva20.wayportals.portal;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPortalManager {

  /**
   * Tries to load a portal at the given location
   *
   * @return The portal found at [location] null if none is found
   */
  @Nullable Portal get(Location location);

  @Nullable Portal find(@Nullable Long id);

  @Nullable Portal findLink(@Nullable Long id);

  void update(@NotNull Portal portal);

  void delete(@NotNull Portal portal);

  void link(@NotNull Portal portal, @NotNull Portal target);
}
