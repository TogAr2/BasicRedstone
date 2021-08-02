package io.github.bloepiloepi.basicredstone.redstone;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public interface RedstoneComponent {
	boolean is(Block block);
	void onPower(Instance instance, Point position, Block block);
	void onLosePower(Instance instance, Point position, Block block);
}
