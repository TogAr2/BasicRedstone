package io.github.bloepiloepi.basicredstone.test;

import io.github.bloepiloepi.basicredstone.door.Doors;
import io.github.bloepiloepi.basicredstone.door.Trapdoors;
import io.github.bloepiloepi.basicredstone.redstone.PowerNet;
import io.github.bloepiloepi.basicredstone.redstone.Redstone;
import io.github.bloepiloepi.basicredstone.redstone.sources.Buttons;
import io.github.bloepiloepi.basicredstone.redstone.sources.Lever;
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
		
		GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();
		
		eventHandler.addChild(Doors.events());
		eventHandler.addChild(Trapdoors.events());
		
		eventHandler.addChild(Buttons.events());
		eventHandler.addChild(Lever.events());
		
		PowerNet powerNet = Redstone.getPowerNet(instance);
		powerNet.useAllReactors();
		
		OpenToLAN.open();
		
		server.start("localhost", 25565);
	}
}
