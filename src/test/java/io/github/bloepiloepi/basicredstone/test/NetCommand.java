package io.github.bloepiloepi.basicredstone.test;

import io.github.bloepiloepi.basicredstone.redstone.PowerNet;
import io.github.bloepiloepi.basicredstone.redstone.Redstone;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.Player;

public class NetCommand extends Command {
	
	public NetCommand() {
		super("net");
		
		setCondition(Conditions::playerOnly);
		
		setDefaultExecutor((sender, args) -> {
			Player player = sender.asPlayer();
			PowerNet powerNet = Redstone.getPowerNet(player.getInstance());
			player.sendMessage(powerNet.toString());
		});
	}
}
