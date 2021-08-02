package io.github.bloepiloepi.basicredstone.redstone;

import io.github.bloepiloepi.basicredstone.door.Doors;
import io.github.bloepiloepi.basicredstone.door.Trapdoors;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class PowerNet {
	private final Instance instance;
	private final List<RedstoneReactor> reactors = new ArrayList<>();
	private final Set<Point> poweredPoints = new HashSet<>();
	private final Set<Point> indirectPowerPoints = new HashSet<>();
	
	public PowerNet(Instance instance) {
		this.instance = instance;
	}
	
	public void useAllReactors() {
		useReactor(Doors.DOOR_REACTOR);
		useReactor(Trapdoors.TRAPDOOR_REACTOR);
	}
	
	public void useReactor(RedstoneReactor reactor) {
		reactors.add(reactor);
	}
	
	public Set<RedstoneReactor> findReactors(Block block) {
		return reactors.stream().filter(reactor -> reactor.is(block)).collect(Collectors.toSet());
	}
	
	public void forEachSide(Point position, BiConsumer<Point, Block> consumer) {
		for (BlockFace face : BlockFace.values()) {
			Point blockPos = position.relative(face);
			Block block = instance.getBlock(blockPos);
			
			consumer.accept(blockPos, block);
		}
	}
	
	public boolean hasPower(Point position) {
		return poweredPoints.contains(position) || indirectPowerPoints.contains(position);
	}
	
	private boolean shouldNotBePowered(Point position) {
		if (poweredPoints.contains(position)) return false;
		
		// Check adjacent points
		for (Point point : poweredPoints) {
			boolean xEqual = false;
			boolean yEqual = false;
			
			int equalCount = 0;
			if (point.blockX() == position.blockX()) {
				xEqual = true;
				equalCount++;
			}
			if (point.blockY() == position.blockY()) {
				yEqual = true;
				equalCount++;
			}
			if (point.blockZ() == position.blockZ()) {
				equalCount++;
			}
			
			if (equalCount < 2) continue;
			
			if (!xEqual) {
				if (Math.abs(point.blockX() - position.blockX()) == 1) {
					return false;
				}
			} else if (!yEqual) {
				if (Math.abs(point.blockY() - position.blockY()) == 1) {
					return false;
				}
			} else {
				if (Math.abs(point.blockZ() - position.blockZ()) == 1) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public void setPower(Point position, boolean power) {
		if (power) {
			power(position);
		} else {
			losePower(position);
		}
	}
	
	public void power(Point position) {
		if (poweredPoints.contains(position)) return;
		
		// Power the position itself as well
		Block middleBlock = instance.getBlock(position);
		findReactors(middleBlock).forEach(reactor -> reactor.onPower(instance, position, middleBlock));
		
		forEachSide(position, (blockPos, block) -> {
			if (hasPower(blockPos)) return;
			indirectPowerPoints.add(blockPos);
			
			findReactors(block).forEach(reactor -> reactor.onPower(instance, blockPos, block));
		});
		
		poweredPoints.add(position);
		indirectPowerPoints.remove(position);
	}
	
	public void losePower(Point position) {
		if (!poweredPoints.remove(position)) return;
		
		// Un-power the position itself as well
		if (shouldNotBePowered(position)) {
			Block block = instance.getBlock(position);
			findReactors(block).forEach(reactor -> reactor.onLosePower(instance, position, block));
		} else {
			if (!hasPower(position)) {
				indirectPowerPoints.add(position);
			}
		}
		
		forEachSide(position, (blockPos, block) -> {
			if (shouldNotBePowered(blockPos)) {
				indirectPowerPoints.remove(blockPos);
				findReactors(block).forEach(reactor -> reactor.onLosePower(instance, blockPos, block));
			} else {
				if (!hasPower(blockPos)) {
					indirectPowerPoints.add(blockPos);
				}
			}
		});
	}
	
	@Override
	public String toString() {
		return "PowerNet{" +
				"poweredPoints=" + poweredPoints +
				", indirectPowerPoints=" + indirectPowerPoints +
				'}';
	}
}
