package io.github.bloepiloepi.basicredstone.test;

import io.github.bloepiloepi.basicredstone.BasicRedstoneExtension;
import io.github.bloepiloepi.basicredstone.redstone.PowerNet;
import io.github.bloepiloepi.basicredstone.redstone.Redstone;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.extras.lan.OpenToLAN;
import net.minestom.server.instance.Instance;

public class RedstoneTest {
	public static void main(String[] args) {
		MinecraftServer server = MinecraftServer.init();
		
		Instance instance = MinecraftServer.getInstanceManager().createInstanceContainer();
		instance.setChunkGenerator(new DemoGenerator());
		instance.enableAutoChunkLoad(true);
		
		MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, event -> {
			event.setSpawningInstance(instance);
			event.getPlayer().setRespawnPoint(new Pos(0, 60, 0));
		});
		
		MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, event ->
				event.getPlayer().setGameMode(GameMode.CREATIVE));
		
		MinecraftServer.getCommandManager().register(new NetCommand());
		
		OpenToLAN.open();
		
		server.start("localhost", 25565);
	}
}
