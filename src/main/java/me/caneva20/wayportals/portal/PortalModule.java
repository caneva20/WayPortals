package me.caneva20.wayportals.portal;

import dagger.Binds;
import dagger.Module;
import me.caneva20.wayportals.portal.db.IPortalDatabase;
import me.caneva20.wayportals.portal.db.PortalDatabase;

@Module
public abstract class PortalModule {

  @Binds
  abstract IPortalManager bindPortalManager(PortalManager impl);

  @Binds
  abstract IPortalDatabase bindPortalDatabase(PortalDatabase impl);
}
