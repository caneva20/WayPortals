package me.caneva20.wayportals;

import co.aikar.commands.PaperCommandManager;
import me.caneva20.messagedispatcher.dispachers.IConsoleMessageDispatcher;
import me.caneva20.wayportals.commands.TestCommand;
import me.caneva20.wayportals.events.BindingEventHandler;
import me.caneva20.wayportals.events.InteractionEventHandler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;

public final class WayPortals extends JavaPlugin {
    @Inject
    private IConsoleMessageDispatcher dispatcher;

    @Inject
    private InteractionEventHandler interactionEventHandler;

    @Inject
    private BindingEventHandler bindingEventHandler;

    @Inject
    private PaperCommandManager commandManager;

    @Inject
    private PluginManager pluginManager;

    @Inject DatabaseHandler database;

    @Override
    public void onEnable() {
        var injector = new BinderModule(this).createInjector();

        injector.injectMembers(this);

        pluginManager.registerEvents(interactionEventHandler, this);
        pluginManager.registerEvents(bindingEventHandler, this);

        database.initialize(this);

        dispatcher.success("Enabled! :)");
    }

    @Override
    public void onDisable() {
        database.close();

        dispatcher.success("Disabled :(");
    }
}
