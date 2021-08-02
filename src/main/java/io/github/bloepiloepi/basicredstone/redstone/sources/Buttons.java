package io.github.bloepiloepi.basicredstone.redstone.sources;

import io.github.bloepiloepi.basicredstone.redstone.PowerNet;
import io.github.bloepiloepi.basicredstone.redstone.Redstone;
import io.github.bloepiloepi.basicredstone.util.AttachFace;
import io.github.bloepiloepi.basicredstone.util.InstanceUtil;
import io.github.bloepiloepi.basicredstone.util.StateUtil;
import it.unimi.dsi.fastutil.Pair;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
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
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Buttons {
	private static final Set<Pair<Instance, Point>> arrowInside = new HashSet<>();
	
	public static boolean hasArrowInside(Instance instance, Point position) {
		for (Pair<Instance, Point> pair : arrowInside) {
			if (pair.key().equals(instance) && pair.value().equals(position)) return true;
		}
		
		return false;
	}
	
	public static boolean isButton(Block block) {
		return Block.DARK_OAK_BUTTON.compare(block)
				|| Block.ACACIA_BUTTON.compare(block)
				|| Block.BIRCH_BUTTON.compare(block)
				|| Block.CRIMSON_BUTTON.compare(block)
				|| Block.JUNGLE_BUTTON.compare(block)
				|| Block.OAK_BUTTON.compare(block)
				|| Block.SPRUCE_BUTTON.compare(block)
				|| Block.WARPED_BUTTON.compare(block)
				|| Block.POLISHED_BLACKSTONE_BUTTON.compare(block)
				|| Block.STONE_BUTTON.compare(block);
	}
	
	public static boolean isStone(Block block) {
		return Block.STONE_BUTTON.compare(block) || Block.POLISHED_BLACKSTONE_BUTTON.compare(block);
	}
	
	public static boolean isSensitive(Block block) {
		return !isStone(block);
	}
	
	public static int getPressDuration(Block block) {
		return isSensitive(block) ? 30 : 20;
	}
	
	public static void playSound(Instance instance, Point position, Block block,
	                             boolean pressed, @Nullable Player source) {
		SoundEvent soundEvent;
		if (isStone(block)) {
			soundEvent = pressed ? SoundEvent.BLOCK_STONE_BUTTON_CLICK_ON
					: SoundEvent.BLOCK_STONE_BUTTON_CLICK_OFF;
		} else {
			soundEvent = pressed ? SoundEvent.BLOCK_WOODEN_BUTTON_CLICK_ON
					: SoundEvent.BLOCK_WOODEN_BUTTON_CLICK_OFF;
		}
		
		InstanceUtil.sendNearby(pressed ? source : null, instance, position.add(0.5, 0.5, 0.5),
				Sound.sound(soundEvent.key(), Sound.Source.BLOCK, 0.3F, pressed ? 0.6F : 0.5F));
	}
	
	public static void power(Instance instance, Point position, Block block,
	                         boolean power, @Nullable Player source) {
		boolean wasPowered = StateUtil.isPowered(block);
		if (power != wasPowered) {
			instance.setBlock(position, block.withProperty("powered", Boolean.toString(power)));
			powerAdjacent(instance, position, block, power);
			playSound(instance, position, block, power, source);
		}
		
		if (power) {
			queueLosePower(instance, position, block);
		}
	}
	
	public static void queueLosePower(Instance instance, Point position, Block block) {
		MinecraftServer.getSchedulerManager().buildTask(() -> {
			Block newBlock = instance.getBlock(position);
			if (!isButton(newBlock)) return;
			
			if (!hasArrowInside(instance, position)) {
				power(instance, position, newBlock, false, null);
			}
		}).delay(getPressDuration(block), TimeUnit.SERVER_TICK).schedule();
	}
	
	public static void arrowInside(Instance instance, Point position) {
		arrowInside.add(Pair.of(instance, position));
		
		Block block = instance.getBlock(position);
		if (!StateUtil.isPowered(block) && isSensitive(block)) {
			power(instance,position, block, true, null);
		}
	}
	
	public static void arrowRemoved(Instance instance, Point position) {
		arrowInside.remove(Pair.of(instance, position));
		
		Block block = instance.getBlock(position);
		if (StateUtil.isPowered(block) && isSensitive(block)) {
			queueLosePower(instance, position, block);
		}
	}
	
	public static void powerAdjacent(Instance instance, Point position, Block block, boolean power) {
		PowerNet powerNet = Redstone.getPowerNet(instance);
		
		powerNet.setPower(position, power);
		powerNet.setPower(position.relative(StateUtil.getConnectedBlockFace(block).getOppositeFace()), power);
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
			if (!isButton(block)) return;
			if (StateUtil.isPowered(block)) return;
			event.setBlockingItemUse(true);
			
			power(instance, position, block, true, event.getPlayer());
		}).ignoreCancelled(false).build());
		
		node.addListener(EventListener.builder(PlayerBlockBreakEvent.class).handler(event -> {
			Instance instance = event.getPlayer().getInstance();
			Point position = event.getBlockPosition();
			assert instance != null;
			
			Block block = instance.getBlock(position);
			if (!isButton(block)) return;
			
			arrowInside.remove(Pair.of(instance, position));
			powerAdjacent(instance, position, block, false);
		}).ignoreCancelled(false).build());
		
		return node;
	}
}
