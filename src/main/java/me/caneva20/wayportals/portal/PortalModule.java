package me.caneva20.wayportals.portal;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;
import me.caneva20.wayportals.portal.db.IPortalDatabase;
import me.caneva20.wayportals.portal.db.PortalDatabase;
import me.caneva20.wayportals.portal.eventhandlers.TeleportationEventHandler;
import org.bukkit.event.Listener;

@Module
public abstract class PortalModule {

  @Binds
  abstract IPortalManager bindPortalManager(PortalManager impl);

  @Binds
  abstract IPortalDatabase bindPortalDatabase(PortalDatabase impl);

  @Binds
  @IntoSet
  abstract Listener bindTeleportationEventHandler(TeleportationEventHandler impl);
}
