package me.caneva20.wayportals.teleport;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class TeleportModule {

  @Binds
  abstract ITeleportManager bindTeleportManager(TeleportManager impl);
}
