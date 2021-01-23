package me.caneva20.wayportals.portalbinder;

import dagger.Module;
import dagger.Provides;

@Module
public class PortalBinderModule {

  @Provides
  static IPortalBinderManager providePortalBinderManager(PortalBinderManager impl) {
    return impl;
  }
}
