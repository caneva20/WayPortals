package me.caneva20.wayportals.portalbinder;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;
import javax.inject.Singleton;
import me.caneva20.wayportals.portalbinder.config.BinderConfig;
import me.caneva20.wayportals.portalbinder.config.IBinderConfig;
import me.caneva20.wayportals.portalbinder.eventhandlers.DropEventHandler;
import me.caneva20.wayportals.portalbinder.eventhandlers.InteractionEventHandler;
import me.caneva20.wayportals.portalbinder.eventhandlers.PreventInteractionEventHandler;
import org.bukkit.event.Listener;

@Module
public abstract class PortalBinderModule {

  @Binds
  @Singleton
  abstract IBinderConfig provideDropperConfig(BinderConfig impl);

  @Binds
  abstract IPortalBinderManager providePortalBinderManager(PortalBinderManager impl);

  @Binds
  @IntoSet
  abstract Listener provideDropEventHandler(DropEventHandler impl);

  @Binds
  @IntoSet
  abstract Listener provideInteractionEventHandler(InteractionEventHandler impl);

  @Binds
  @IntoSet
  abstract Listener providePreventInteractionEventHandler(PreventInteractionEventHandler impl);
}
