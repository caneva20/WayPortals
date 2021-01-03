package me.caneva20.wayportals.items;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import me.caneva20.messagedispatcher.dispachers.IConsoleMessageDispatcher;
import me.caneva20.wayportals.KeyProvider;
import me.caneva20.wayportals.WayPortals;
import me.caneva20.wayportals.items.portalbinder.PortalBinderUtility;
import me.caneva20.wayportals.utils.data.PersistentLocation;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Objects;

@AutoFactory
public class PortalBinder implements IPortalBinder {
    private final WayPortals plugin;
    private final KeyProvider keys;
    private final PersistentLocation persistentLocation;
    private final IConsoleMessageDispatcher consoleDispatcher;

    private final ItemStack stack;

    @Inject
    PortalBinder(
            @Provided WayPortals plugin,
            @Provided KeyProvider keys,
            @Provided PersistentLocation persistentLocation,
            @Provided PortalBinderUtility binderUtility,
            @Provided IConsoleMessageDispatcher consoleDispatcher,
            ItemStack stack
    ) {
        this.plugin = plugin;
        this.keys = keys;
        this.persistentLocation = persistentLocation;
        this.consoleDispatcher = consoleDispatcher;
        this.stack = stack;

        if (!binderUtility.isBinder(stack)) {
            transform();
        }
    }

    private void transform() {
        var portalBinderKey = keys.getPortalBinderKey();

        var meta = plugin.getServer().getItemFactory().getItemMeta(stack.getType());

        if (meta == null) {
            consoleDispatcher.error("This item stack can't be transformed into a portal binder!");

            return;
        }

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        meta.getPersistentDataContainer().set(portalBinderKey, PersistentDataType.INTEGER, 1);

        stack.setItemMeta(meta);
        stack.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);

        updateLore();
    }

    @NotNull
    private ItemMeta getMeta() {
        return Objects.requireNonNull(stack.getItemMeta());
    }

    private boolean updateMeta(ItemMeta meta) {
        return stack.setItemMeta(meta);
    }

    @Nullable
    public Location getTarget() {
        if (!hasTarget()) {
            return null;
        }

        var key = keys.getPortalBinderTargetKey();

        var container = getMeta().getPersistentDataContainer();

        return container.get(key, persistentLocation);
    }

    public void setTarget(Location location) {
        var meta = getMeta();

        meta.getPersistentDataContainer().set(keys.getPortalBinderTargetKey(), persistentLocation, location);

        updateMeta(meta);
        updateLore();
    }

    public boolean hasTarget() {
        var key = keys.getPortalBinderTargetKey();

        var container = getMeta().getPersistentDataContainer();

        return container.has(key, persistentLocation);
    }

    private void updateLore() {
        var lore = new ArrayList<String>();
        var target = getTarget();

        lore.add("");

        if (target != null) {
            lore.add(String.format("§eBound to: §6%s@%s,%s,%s", Objects.requireNonNull(target.getWorld()).getName(),
                    target.getX(), target.getY(), target.getZ()));
        } else {
            lore.add("§eBound to: §6(nowhere)");
        }

        lore.add("");

        var meta = getMeta();

        meta.setLore(lore);

        updateMeta(meta);
    }
}
