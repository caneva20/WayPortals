package me.caneva20.wayportals.signs;

import dagger.Binds;
import dagger.Module;
import me.caneva20.wayportals.signs.db.ISignDatabase;
import me.caneva20.wayportals.signs.db.SignDatabase;

@Module
public abstract class SignModule {

  @Binds
  abstract ISignDatabase bindSignDatabase(SignDatabase impl);
}
