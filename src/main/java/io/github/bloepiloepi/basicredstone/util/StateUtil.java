package io.github.bloepiloepi.basicredstone.util;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;

public class StateUtil {
	
	public static boolean isPowered(Block block) {
		return block.getProperty("powered").equals("true");
	}
	
	public static boolean isOpen(Block block) {
		return block.getProperty("open").equals("true");
	}
	
	public static AttachFace getFace(Block block) {
		return AttachFace.byName(block.getProperty("face"));
	}
	
	public static BlockFace getFacing(Block block) {
		String directionName = block.getProperty("facing");
		
		for (BlockFace face : BlockFace.values()) {
			if (face.toDirection().name().equalsIgnoreCase(directionName)) {
				return face;
			}
		}
		
		return null;
	}
	
	public static BlockFace getConnectedBlockFace(Block block) {
		switch (StateUtil.getFace(block)) {
			case FLOOR:
				return BlockFace.TOP;
			case CEILING:
				return BlockFace.BOTTOM;
			default:
				return StateUtil.getFacing(block);
		}
	}
}
