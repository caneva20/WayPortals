package me.caneva20.wayportals;

import co.aikar.commands.PaperCommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import me.caneva20.messagedispatcher.Messaging;
import me.caneva20.messagedispatcher.dispachers.IConsoleMessageDispatcher;
import me.caneva20.messagedispatcher.dispachers.IMessageDispatcher;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BinderModule extends AbstractModule {
    private final WayPortals plugin;

    public BinderModule(WayPortals plugin) {
        this.plugin = plugin;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {
        super.configure();

        bind(WayPortals.class).toInstance(plugin);
        bind(JavaPlugin.class).toInstance(plugin);

        bind(PaperCommandManager.class).toInstance(new PaperCommandManager(plugin));
        bind(PluginManager.class).toInstance(plugin.getServer().getPluginManager());

        bind(IConsoleMessageDispatcher.class).toInstance(Messaging.createConsoleDispatcher(plugin, true));
        bind(IMessageDispatcher.class).toInstance(Messaging.createDispatcher(plugin, true));
    }
}
