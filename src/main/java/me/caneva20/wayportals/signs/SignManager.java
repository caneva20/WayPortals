package me.caneva20.wayportals.signs;

import javax.inject.Inject;
import lombok.val;
import me.caneva20.wayportals.portal.IPortalManager;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.signs.db.ISignDatabase;
import me.caneva20.wayportals.signs.db.SignRecord;
import me.caneva20.wayportals.utils.WorldVector3;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SignManager implements ISignManager {

  private final ISignDatabase db;
  private final IPortalManager portalManager;

  @Inject
  SignManager(ISignDatabase db, IPortalManager portalManager) {
    this.db = db;
    this.portalManager = portalManager;
  }

  @Override
  @Nullable
  public PortalSign get(Sign sign) {
    var record = db.find(new WorldVector3(sign.getLocation()));

    return create(record, sign, null);
  }

  @Override
  @Nullable
  public PortalSign get(Portal portal) {
    val record = db.findForPortal(portal.id());

    if (record == null) {
      return null;
    }

    val world = Bukkit.getWorld(record.world());

    if (world == null) {
      return null;
    }

    val block = world.getBlockAt(record.x(), record.y(), record.z());

    if (block.getState() instanceof Sign) {
      return create(record, (Sign) block.getState(), portal);
    }

    db.delete(record.id());

    return null;
  }

  @Override
  @Nullable
  public PortalSign create(@NotNull Sign sign, @NotNull Portal portal) {
    if (get(portal) != null) {
      return null;
    }

    val record = db.create(new WorldVector3(sign.getLocation()), (int) portal.id());

    return create(record, sign, portal);
  }

  @Nullable
  private PortalSign create(@Nullable SignRecord record, @NotNull Sign sign,
      @Nullable Portal portal) {

    if (record == null) {
      return null;
    }

    if (portal == null) {
      portal = portalManager.find(record.portalId());
    }

    return new PortalSign(record.id(), record.location(), sign, portal);
  }
}
