package me.caneva20.wayportals.portal;

import java.util.Collections;
import java.util.stream.Collectors;
import me.caneva20.wayportals.utils.Vector3;
import me.caneva20.wayportals.utils.Vector3Int;
import me.caneva20.wayportals.utils.VeinUtility;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

public class PortalUtility {
  private PortalUtility() {
  }

  @Nullable
  public static Portal find(Block portalBlock) {
    if (portalBlock.getType() != Material.NETHER_PORTAL) {
      return null;
    }

    var veinBlocks = VeinUtility.getVein(portalBlock, 441, false);

    var vein = veinBlocks.stream()
        .map(x -> new Vector3(x.getX(), x.getY(), x.getZ()))
        .collect(Collectors.toList());

    var xs = vein.stream().map(Vector3::x).collect(Collectors.toList());
    var ys = vein.stream().map(Vector3::y).collect(Collectors.toList());
    var zs = vein.stream().map(Vector3::z).collect(Collectors.toList());

    var minX = Collections.min(xs);
    var minY = Collections.min(ys);
    var minZ = Collections.min(zs);

    var maxX = Collections.max(xs);
    var maxY = Collections.max(ys);
    var maxZ = Collections.max(zs);

    return new Portal(new Vector3Int(minX, minY, minZ), new Vector3Int(maxX, maxY, maxZ),
        portalBlock.getWorld().getName());
  }
}
