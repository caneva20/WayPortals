package me.caneva20.wayportals;

import org.bukkit.NamespacedKey;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class KeyProvider {

  private static final String portalBinderStringKey = "portal_binder";
  private static final String portalBinderTargetStringKey = "portal_binder_location";

  private final WayPortals plugin;

  @Inject
  KeyProvider(WayPortals plugin) {
    this.plugin = plugin;
  }

  public NamespacedKey getPortalBinderKey() {
    return new NamespacedKey(plugin, portalBinderStringKey);
  }

  public NamespacedKey getPortalBinderTargetKey() {
    return new NamespacedKey(plugin, portalBinderTargetStringKey);
  }
}
