package io.github.bloepiloepi.basicredstone.door;

import io.github.bloepiloepi.basicredstone.redstone.Redstone;
import io.github.bloepiloepi.basicredstone.redstone.RedstoneReactor;
import io.github.bloepiloepi.basicredstone.util.InstanceUtil;
import io.github.bloepiloepi.basicredstone.util.StateUtil;
import net.minestom.server.coordinate.Point;
import net.minestom.server.effects.Effects;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.trait.EntityEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import org.jetbrains.annotations.Nullable;

public class Doors {
	public static final RedstoneReactor DOOR_REACTOR = new RedstoneReactor() {
		@Override
		public boolean is(Block block) {
			return isDoor(block);
		}
		
		@Override
		public void onPower(Instance instance, Point position, Block block) {
			if (StateUtil.isPowered(block)) return;
			
			Point otherPos = position.relative(isBottomHalf(block) ? BlockFace.TOP : BlockFace.BOTTOM);
			Block otherBlock = instance.getBlock(otherPos);
			
			setOpen(instance, position, true, true, true, null);
			if (isDoor(otherBlock)) {
				setOpen(instance, otherPos, true, true, false, null);
			}
		}
		
		@Override
		public void onLosePower(Instance instance, Point position, Block block) {
			Point otherPos = position.relative(isBottomHalf(block) ? BlockFace.TOP : BlockFace.BOTTOM);
			Block otherBlock = instance.getBlock(otherPos);
			
			boolean hasOther = isDoor(otherBlock);
			if (hasOther && Redstone.getPowerNet(instance).hasPower(otherPos)) {
				// Don't close this door when the other door block has power
				return;
			}
			
			setOpen(instance, position, false, false, true, null);
			if (hasOther) {
				setOpen(instance, otherPos, false, false, false, null);
			}
		}
	};
	
	public static Effects getCloseEffect(Block block) {
		return isMetal(block) ? Effects.IRON_DOOR_CLOSED : Effects.WOODEN_DOOR_CLOSED;
	}
	
	public static Effects getOpenEffect(Block block) {
		return isMetal(block) ? Effects.IRON_DOOR_OPENED : Effects.WOODEN_DOOR_OPENED;
	}
	
	public static boolean isMetal(Block block) {
		return Block.IRON_DOOR.compare(block);
	}
	
	public static boolean isDoor(Block block) {
		return Block.IRON_DOOR.compare(block)
				|| Block.DARK_OAK_DOOR.compare(block)
				|| Block.ACACIA_DOOR.compare(block)
				|| Block.BIRCH_DOOR.compare(block)
				|| Block.CRIMSON_DOOR.compare(block)
				|| Block.JUNGLE_DOOR.compare(block)
				|| Block.OAK_DOOR.compare(block)
				|| Block.SPRUCE_DOOR.compare(block)
				|| Block.WARPED_DOOR.compare(block);
	}
	
	public static boolean isBottomHalf(Block block) {
		return block.getProperty("half").equals("lower");
	}
	
	public static void playEffect(Instance instance, Block block, Point position,
	                              @Nullable Player source, boolean open) {
		Effects effect = open ? getOpenEffect(block) : getCloseEffect(block);
		InstanceUtil.sendNearby(source, instance, effect,
				position.blockX(), position.blockY(), position.blockZ(),
				0, 64.0D, false);
	}
	
	public static void setOpen(Instance instance, Point position, boolean open, boolean powered,
	                           boolean playEffect, @Nullable Player source) {
		Block block = instance.getBlock(position);
		
		// Play effect only if state changed
		boolean shouldPlayEffect = playEffect && StateUtil.isOpen(block) != open;
		
		instance.setBlock(position, block
				.withProperty("open", Boolean.toString(open))
				.withProperty("powered", Boolean.toString(powered))
		);
		
		if (shouldPlayEffect) {
			playEffect(instance, block, position, source, open);
		}
	}
	
	public static EventNode<EntityEvent> events() {
		EventNode<EntityEvent> node = EventNode.type("door-events", EventFilter.ENTITY);
		
		node.addListener(EventListener.builder(PlayerBlockInteractEvent.class).handler(event -> {
			if (event.getPlayer().isSneaking() && event.getPlayer()
					.getItemInHand(event.getHand()).getMaterial().isBlock()) return;
			
			Instance instance = event.getPlayer().getInstance();
			Point position = event.getBlockPosition();
			assert instance != null;
			
			Block block = instance.getBlock(position);
			if (isMetal(block)) return; // Metal doors cannot be opened by players
			if (!isDoor(block)) return;
			event.setBlockingItemUse(true); // Make sure no other interactions happen
			
			setOpen(instance, position, !StateUtil.isOpen(block),
					StateUtil.isPowered(block), true, event.getPlayer());
		}).ignoreCancelled(false).build());
		
		return node;
	}
}
