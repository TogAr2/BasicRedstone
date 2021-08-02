package io.github.bloepiloepi.basicredstone.redstone.sources;

import io.github.bloepiloepi.basicredstone.util.AttachFace;
import io.github.bloepiloepi.basicredstone.util.BlockShape;
import io.github.bloepiloepi.basicredstone.util.StateUtil;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;

import java.util.Objects;

public class ButtonShapes {
	public static final BlockShape CEILING_X = new BlockShape(6, 14, 5, 4, 2, 6);
	public static final BlockShape CEILING_Z = new BlockShape(5, 14, 6, 6, 2, 4);
	public static final BlockShape FLOOR_X = new BlockShape(6, 0, 5, 4, 2, 6);
	public static final BlockShape FLOOR_Z = new BlockShape(5, 0, 6, 6, 2, 4);
	public static final BlockShape NORTH = new BlockShape(5, 6, 14, 6, 4, 2);
	public static final BlockShape SOUTH = new BlockShape(5, 6, 0, 6, 4, 2);
	public static final BlockShape WEST = new BlockShape(14, 6, 5, 2, 4, 6);
	public static final BlockShape EAST = new BlockShape(0, 6, 5, 2, 4, 6);
	
	public static final BlockShape PRESSED_CEILING_X = new BlockShape(6, 15, 5, 4, 1, 6);
	public static final BlockShape PRESSED_CEILING_Z = new BlockShape(5, 15, 6, 6, 1, 4);
	public static final BlockShape PRESSED_FLOOR_X = new BlockShape(6, 0, 5, 4, 1, 6);
	public static final BlockShape PRESSED_FLOOR_Z = new BlockShape(5, 0, 6, 6, 1, 4);
	public static final BlockShape PRESSED_NORTH = new BlockShape(5, 6, 15, 6, 4, 1);
	public static final BlockShape PRESSED_SOUTH = new BlockShape(5, 6, 0, 6, 4, 1);
	public static final BlockShape PRESSED_WEST = new BlockShape(15, 6, 5, 1, 4, 6);
	public static final BlockShape PRESSED_EAST = new BlockShape(0, 6, 5, 1, 4, 6);
	
	public static BlockShape getShape(Block block) {
		BlockFace facing = StateUtil.getFacing(block);
		AttachFace face = StateUtil.getFace(block);
		boolean powered = StateUtil.isPowered(block);
		
		switch (face) {
			case FLOOR:
				if (facing == BlockFace.WEST || facing == BlockFace.EAST) {
					return powered ? PRESSED_FLOOR_X : FLOOR_X;
				} else {
					return powered ? PRESSED_FLOOR_Z : FLOOR_Z;
				}
			case WALL:
				switch (Objects.requireNonNull(facing)) {
					case SOUTH:
						return powered ? PRESSED_SOUTH : SOUTH;
					case WEST:
						return powered ? PRESSED_WEST : WEST;
					case EAST:
						return powered ? PRESSED_EAST : EAST;
					default:
						return powered ? PRESSED_NORTH : NORTH;
				}
			case CEILING:
			default:
				if (facing == BlockFace.WEST || facing == BlockFace.EAST) {
					return powered ? PRESSED_CEILING_X : CEILING_X;
				} else {
					return powered ? PRESSED_CEILING_Z : CEILING_Z;
				}
		}
	}
}
