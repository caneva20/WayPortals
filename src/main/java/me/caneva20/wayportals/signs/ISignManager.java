package me.caneva20.wayportals.signs;

import me.caneva20.wayportals.portal.Portal;
import org.bukkit.block.Sign;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ISignManager {

  /**
   * Tries to find a PortalSign at the given sign position
   *
   * @param sign the sign to search with
   * @return the PortalSign at that position or null if none is found
   */
  @Nullable PortalSign get(Sign sign);

  /**
   * Tries to find a PortalSign for the given portal
   *
   * @param portal the portal to look a PortalSign for
   * @return the PortalSign of the given portal or null if none is found
   */
  @Nullable PortalSign get(Portal portal);

  /**
   * Tries to create a PortalSign for the given portal with the given sign
   * @param sign the sign use for creation
   * @param portal the portal to create the PortalSign for
   * @return the PortalSign created or null if none was create or null if the given portal
   * already have PortalSign assigned to it
   */
  @Nullable PortalSign create(@NotNull Sign sign, @NotNull Portal portal);
}
