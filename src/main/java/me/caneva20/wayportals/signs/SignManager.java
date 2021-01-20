package me.caneva20.wayportals.signs;

import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.caneva20.wayportals.portal.IPortalManager;
import me.caneva20.wayportals.portal.Portal;
import me.caneva20.wayportals.signs.db.ISignDatabase;
import me.caneva20.wayportals.signs.db.SignRecord;
import me.caneva20.wayportals.utils.WorldVector3;
import org.bukkit.block.Sign;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class SignManager implements ISignManager {

  private final ISignDatabase db;
  private final IPortalManager portalManager;

  @Override
  public @Nullable PortalSign get(Sign sign) {
    var record = db.find(new WorldVector3(sign.getLocation()));

    if (record == null) {
      return null;
    }

    return create(record, sign, null);
  }

  @Override
  public @Nullable PortalSign create(@NotNull Sign sign, @NotNull Portal portal) {
    val record = db.create(new WorldVector3(sign.getLocation()), (int) portal.id());

    if (record == null) {
      return null;
    }

    return create(record, sign, portal);
  }

  private @NotNull PortalSign create(@NotNull SignRecord record, @NotNull Sign sign,
      @Nullable Portal portal) {
    if (portal == null) {
      portal = portalManager.find(record.portalId());
    }

    return new PortalSign(record.id(), record.location(), sign, portal);
  }
}
