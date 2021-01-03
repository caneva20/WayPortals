package me.caneva20.wayportals;

import co.aikar.commands.PaperCommandManager;
import me.caneva20.messagedispatcher.dispachers.IConsoleMessageDispatcher;
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
    private PaperCommandManager commandManager;

    @Inject
    private PluginManager pluginManager;

    @Override
    public void onEnable() {
        var injector = new BinderModule(this).createInjector();

        injector.injectMembers(this);

        pluginManager.registerEvents(interactionEventHandler, this);

        dispatcher.success("Enabled! :)");
    }

    @Override
    public void onDisable() {
    }
}
