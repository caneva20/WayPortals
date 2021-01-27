package me.caneva20.wayportals.teleport;

import me.caneva20.wayportals.portal.Portal;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ITeleportManager {

  @Nullable Location getDestination(@NotNull Portal portal, @NotNull Location playerPosition);
}
