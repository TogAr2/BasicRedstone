package io.github.bloepiloepi.basicredstone.util;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.effects.Effects;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.EffectPacket;
import net.minestom.server.utils.PacketUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InstanceUtil {
	
	public static void sendNearby(@Nullable Player source, @NotNull Instance instance, @NotNull Effects effect,
	                              int x, int y, int z, int data, double distance, boolean global) {
		EffectPacket packet = new EffectPacket();
		packet.effectId = effect.getId();
		packet.position = new Pos(x, y, z);
		packet.data = data;
		packet.disableRelativeVolume = global;
		
		double distanceSquaredMax = distance * distance;
		PacketUtils.sendGroupedPacket(instance.getPlayers(), packet, player -> {
			if (player == source) return false;
			
			Pos position = player.getPosition();
			double dx = x - position.x();
			double dy = y - position.y();
			double dz = z - position.z();
			
			return dx * dx + dy * dy + dz * dz < distanceSquaredMax;
		});
	}
	
	public static void sendNearby(@Nullable Player source, @NotNull Instance instance,
	                              Point position, @NotNull Sound sound) {
		double distanceSquaredMax = sound.volume() > 1.0F ? (16.0F * sound.volume()) : 16.0F;
		
		double x = position.blockX() + 0.5;
		double y = position.blockY() + 0.5;
		double z = position.blockZ() + 0.5;
		
		instance.getPlayers().stream()
				.filter(player -> {
					if (player == source) return false;
					
					Pos playerPos = player.getPosition();
					double dx = x - playerPos.x();
					double dy = y - playerPos.y();
					double dz = z - playerPos.z();
					
					return dx * dx + dy * dy + dz * dz < distanceSquaredMax;
				}).forEach(player -> player.playSound(sound, x, y, z));
	}
}
