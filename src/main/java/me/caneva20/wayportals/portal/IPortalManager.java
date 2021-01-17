package me.caneva20.wayportals.portal;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPortalManager {

  @Nullable Portal get(Location location);

  @Nullable Portal find(int id);

  void delete(@NotNull Portal portal);

  void link(@NotNull Portal portal, @Nullable Portal target);
}
