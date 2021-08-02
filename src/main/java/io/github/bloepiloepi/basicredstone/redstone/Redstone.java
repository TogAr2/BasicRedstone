package io.github.bloepiloepi.basicredstone.redstone;

import net.minestom.server.instance.Instance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class which manages the PowerNet instances.
 */
public class Redstone {
	private static final Map<Instance, PowerNet> powerNets = new ConcurrentHashMap<>();
	
	public static PowerNet getPowerNet(Instance instance) {
		return powerNets.computeIfAbsent(instance, PowerNet::new);
	}
	
	public static void deletePowerNet(Instance instance) {
		powerNets.remove(instance);
	}
}
