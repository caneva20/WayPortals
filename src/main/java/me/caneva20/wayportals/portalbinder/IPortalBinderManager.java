package me.caneva20.wayportals.portalbinder;

import me.caneva20.wayportals.portal.Portal;
import org.bukkit.inventory.ItemStack;

public interface IPortalBinderManager {

  PortalBinder get(ItemStack stack);

  void setPortal(ItemStack stack, Portal portal);

  void setPortal(PortalBinder binder, Portal portal);
}
