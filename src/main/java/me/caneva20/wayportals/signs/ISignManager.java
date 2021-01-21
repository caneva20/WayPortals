package me.caneva20.wayportals.signs;

import me.caneva20.wayportals.portal.Portal;
import org.bukkit.block.Sign;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ISignManager {

  @Nullable PortalSign get(Sign sign);

  @Nullable PortalSign get(Portal portal);

  @Nullable PortalSign create(@NotNull Sign sign, @NotNull Portal portal);
}
