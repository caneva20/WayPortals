package me.caneva20.wayportals.portal.db;

import me.caneva20.wayportals.utils.Region;
import org.jetbrains.annotations.Nullable;

public interface IPortalDatabase {

  void initialize();

  @Nullable
  PortalRecord find(long id);

  @Nullable
  PortalRecord find(Region region);

  @Nullable
  PortalRecord create(Region region);

  void delete(long portalId);

  void setLinkId(long portalId, @Nullable Long linkedPortalId);
}
