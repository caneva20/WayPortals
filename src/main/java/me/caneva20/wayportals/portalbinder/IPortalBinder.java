package me.caneva20.wayportals.portalbinder;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public interface IPortalBinder {
    void setTarget(Location location);

    @Nullable Location getTarget();

    boolean hasTarget();
}
