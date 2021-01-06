package me.caneva20.wayportals.portalbinder;

import me.caneva20.wayportals.portal.Portal;
import org.jetbrains.annotations.Nullable;

public interface IPortalBinder {
    void setPortal(Portal portal);

    @Nullable Portal getPortal();

    boolean hasPortal();
}
