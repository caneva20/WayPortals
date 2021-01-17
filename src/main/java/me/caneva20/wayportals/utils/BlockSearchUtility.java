package me.caneva20.wayportals.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.HashSet;
import java.util.Set;

public class BlockSearchUtility {

  private static final Set<Block> buffer = new HashSet<>();
  private static final Set<Block> recent = new HashSet<>();

  private static final BlockFace[] blockFaces = {BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH,
      BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};

  private BlockSearchUtility() {
  }

  public static Set<Block> getVein(Block start, int max, boolean matchData) {
    Set<Block> blocks = new HashSet<>();
    buffer.add(start);

    while (blocks.size() < max) {
      search:
      for (Block block : recent) {
        blocks.add(block);

        for (BlockFace face : blockFaces) {
          Block relative = block.getRelative(face);

          if (blocks.contains(relative) || !match(relative, block, matchData)) {
            continue;
          }

          if (blocks.size() + buffer.size() >= max) {
            break search;
          }

          buffer.add(relative);
        }
      }

      if (buffer.size() == 0) {
        break;
      }

      recent.clear();
      recent.addAll(buffer);
      blocks.addAll(buffer);
      buffer.clear();
    }

    recent.clear();
    buffer.clear();

    return blocks;
  }

  public static Set<Block> findNeighbours(Block block, Material type) {
    var blocks = new HashSet<Block>();

    for (BlockFace face : blockFaces) {
      var relative = block.getRelative(face);

      if (relative.getType() == type) {
        blocks.add(relative);
      }
    }

    return blocks;
  }

  public static Set<Block> findNeighbours(Block block) {
    var blocks = new HashSet<Block>();

    for (BlockFace face : blockFaces) {
      blocks.add(block.getRelative(face));
    }

    return blocks;
  }

  public static Set<Block> findNeighbours(Block block, boolean matchType) {
    if (matchType) {
      return findNeighbours(block, block.getType());
    } else {
      return findNeighbours(block);
    }
  }

  private static boolean match(Block a, Block b, boolean matchData) {
    if (matchData) {
      return a.getType() == b.getType() && a.getState().getData().equals(b.getState().getData());
    }

    return a.getType() == b.getType();
  }
}
