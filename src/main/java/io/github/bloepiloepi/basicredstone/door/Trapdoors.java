package io.github.bloepiloepi.basicredstone.door;

import io.github.bloepiloepi.basicredstone.redstone.RedstoneComponent;
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
import org.jetbrains.annotations.Nullable;

public class Trapdoors {
	public static final RedstoneComponent TRAPDOOR_COMPONENT = new RedstoneComponent() {
		@Override
		public boolean is(Block block) {
			return isTrapdoor(block);
		}
		
		@Override
		public void onPower(Instance instance, Point position, Block block) {
			if (StateUtil.isPowered(block)) return;
			
			setOpen(instance, position, true, true, null);
		}
		
		@Override
		public void onLosePower(Instance instance, Point position, Block block) {
			if (!StateUtil.isPowered(block)) return;
			
			setOpen(instance, position, false, false, null);
		}
	};
	
	public static Effects getCloseEffect(Block block) {
		// ???????????????
		return isMetal(block) ? Effects.IRON_TRAPDOOR_OPENED : Effects.WOODEN_TRAPDOOR_CLOSED;
	}
	
	public static Effects getOpenEffect(Block block) {
		// ???????????????
		return isMetal(block) ? Effects.IRON_TRAPDOOR_CLOSED : Effects.WOODEN_TRAPDOOR_OPENED;
	}
	
	public static boolean isMetal(Block block) {
		return Block.IRON_TRAPDOOR.compare(block);
	}
	
	public static boolean isTrapdoor(Block block) {
		return Block.IRON_TRAPDOOR.compare(block)
				|| Block.DARK_OAK_TRAPDOOR.compare(block)
				|| Block.ACACIA_TRAPDOOR.compare(block)
				|| Block.BIRCH_TRAPDOOR.compare(block)
				|| Block.CRIMSON_TRAPDOOR.compare(block)
				|| Block.JUNGLE_TRAPDOOR.compare(block)
				|| Block.OAK_TRAPDOOR.compare(block)
				|| Block.SPRUCE_TRAPDOOR.compare(block)
				|| Block.WARPED_TRAPDOOR.compare(block);
	}
	
	public static void playEffect(Instance instance, Block block, Point position,
	                              @Nullable Player source, boolean open) {
		Effects effect = open ? getOpenEffect(block) : getCloseEffect(block);
		InstanceUtil.sendNearby(source, instance, effect,
				position.blockX(), position.blockY(), position.blockZ(),
				0, 64.0D, false);
	}
	
	public static void setOpen(Instance instance, Point position, boolean open,
	                           boolean powered, @Nullable Player source) {
		Block block = instance.getBlock(position);
		
		// Play effect only if state changed
		boolean shouldPlayEffect = StateUtil.isOpen(block) != open;
		
		instance.setBlock(position, block
				.withProperty("open", Boolean.toString(open))
				.withProperty("powered", Boolean.toString(powered))
		);
		
		if (shouldPlayEffect) {
			playEffect(instance, block, position, source, open);
		}
	}
	
	/**
	 * Creates an EventNode with the events for manual (no redstone) trapdoor interaction listening.
	 *
	 * @return The EventNode
	 */
	public static EventNode<EntityEvent> events() {
		EventNode<EntityEvent> node = EventNode.type("trapdoor-events", EventFilter.ENTITY);
		
		node.addListener(EventListener.builder(PlayerBlockInteractEvent.class).handler(event -> {
			if (event.getPlayer().isSneaking() && event.getPlayer()
					.getItemInHand(event.getHand()).getMaterial().isBlock()) return;
			
			Instance instance = event.getPlayer().getInstance();
			Point position = event.getBlockPosition();
			assert instance != null;
			
			Block block = instance.getBlock(position);
			if (isMetal(block)) return; // Metal trapdoors cannot be opened by players
			if (!isTrapdoor(block)) return;
			event.setBlockingItemUse(true); // Make sure no other interactions happen
			
			setOpen(instance, position, !StateUtil.isOpen(block),
					StateUtil.isPowered(block), event.getPlayer());
		}).ignoreCancelled(false).build());
		
		return node;
	}
}
