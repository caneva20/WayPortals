package me.caneva20.wayportals.signs;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;
import me.caneva20.wayportals.signs.db.ISignDatabase;
import me.caneva20.wayportals.signs.db.SignDatabase;
import me.caneva20.wayportals.signs.eventhandlers.CreationSignEventHandler;
import me.caneva20.wayportals.signs.eventhandlers.InteractionSignEventHandler;
import me.caneva20.wayportals.signs.eventhandlers.PortalUpdateSignEventHandler;
import org.bukkit.event.Listener;

@Module
public abstract class SignModule {

  @Binds
  abstract ISignManager bindSignManager(SignManager impl);

  @Binds
  abstract ISignContentManager bindSignContentManager(SignContentManager impl);

  @Binds
  abstract ISignDatabase bindSignDatabase(SignDatabase impl);

  @Binds
  @IntoSet
  abstract Listener bindCreationSignEventHandler(CreationSignEventHandler impl);

  @Binds
  @IntoSet
  abstract Listener bindInteractionSignEventHandler(InteractionSignEventHandler impl);

  @Binds
  @IntoSet
  abstract Listener bindPortalUpdateSignEventHandler(PortalUpdateSignEventHandler impl);
}
