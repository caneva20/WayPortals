package me.caneva20.wayportals.portalbinder;

import me.caneva20.wayportals.portal.Portal;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPortalBinderManager {

  @Nullable
  PortalBinder get(ItemStack stack);

  @NotNull
  PortalBinder create();

  void setPortal(@NotNull PortalBinder binder, @NotNull Portal portal);
}
