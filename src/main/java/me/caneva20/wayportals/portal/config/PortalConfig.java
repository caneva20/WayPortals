package me.caneva20.wayportals.portal.config;

import ch.jalu.configme.SettingsManager;
import javax.inject.Inject;
import me.caneva20.wayportals.settings.PluginConfig;

public class PortalConfig implements IPortalConfig {

  private final SettingsManager settingsManager;

  @Inject
  PortalConfig(SettingsManager settingsManager) {
    this.settingsManager = settingsManager;
  }

  @Override
  public BuildPermissionCheckMode buildPermissionCheckMode() {
    return settingsManager.getProperty(PluginConfig.BUILD_PERMISSION_CHECK_MODE);
  }
}
