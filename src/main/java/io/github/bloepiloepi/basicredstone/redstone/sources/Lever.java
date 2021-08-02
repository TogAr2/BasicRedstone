package io.github.bloepiloepi.basicredstone.redstone.sources;

import io.github.bloepiloepi.basicredstone.util.InstanceUtil;
import io.github.bloepiloepi.basicredstone.util.StateUtil;
import it.unimi.dsi.fastutil.Pair;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.trait.EntityEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.sound.SoundEvent;
import org.jetbrains.annotations.Nullable;

public class Lever {
	
	public static boolean isLever(Block block) {
		return Block.LEVER.compare(block);
	}
	
	public static void pull(Instance instance, Point position, Block block, @Nullable Player source) {
		boolean power = !StateUtil.isPowered(block);
		
		instance.setBlock(position, block.withProperty("powered", Boolean.toString(power)));
		Buttons.powerAdjacent(instance, position, block, power);
		
		InstanceUtil.sendNearby(null, instance, position,
				Sound.sound(SoundEvent.BLOCK_LEVER_CLICK.key(),
				Sound.Source.BLOCK, 0.3F, power ? 0.6F : 0.5F));
	}
	
	public static EventNode<EntityEvent> events() {
		EventNode<EntityEvent> node = EventNode.type("button-events", EventFilter.ENTITY);
		
		node.addListener(EventListener.builder(PlayerBlockInteractEvent.class).handler(event -> {
			if (event.getPlayer().isSneaking() && event.getPlayer()
					.getItemInHand(event.getHand()).getMaterial().isBlock()) return;
			
			Instance instance = event.getPlayer().getInstance();
			Point position = event.getBlockPosition();
			assert instance != null;
			
			Block block = instance.getBlock(position);
			if (!isLever(block)) return;
			event.setBlockingItemUse(true);
			
			pull(instance, position, block, event.getPlayer());
		}).ignoreCancelled(false).build());
		
		node.addListener(EventListener.builder(PlayerBlockBreakEvent.class).handler(event -> {
			Instance instance = event.getPlayer().getInstance();
			Point position = event.getBlockPosition();
			assert instance != null;
			
			Block block = instance.getBlock(position);
			if (!isLever(block)) return;
			
			Buttons.powerAdjacent(instance, position, block, false);
		}).ignoreCancelled(false).build());
		
		return node;
	}
}
