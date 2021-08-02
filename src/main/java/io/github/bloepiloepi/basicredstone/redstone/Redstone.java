package io.github.bloepiloepi.basicredstone.redstone;

import net.minestom.server.instance.Instance;

import java.util.HashMap;
import java.util.Map;

public class Redstone {
	private static final Map<Instance, PowerNet> powerNets = new HashMap<>();
	
	public static PowerNet getPowerNet(Instance instance) {
		return powerNets.computeIfAbsent(instance, PowerNet::new);
	}
	
	//TODO instance deletion
}
