package io.github.bloepiloepi.basicredstone;

import io.github.bloepiloepi.basicredstone.door.Doors;
import io.github.bloepiloepi.basicredstone.door.Trapdoors;
import io.github.bloepiloepi.basicredstone.redstone.sources.Buttons;
import io.github.bloepiloepi.basicredstone.redstone.sources.Lever;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.EntityEvent;
import net.minestom.server.extensions.Extension;

public class BasicRedstoneExtension extends Extension {
	public static final EventNode<EntityEvent> REDSTONE_EVENTS = events();
	
	private static EventNode<EntityEvent> events() {
		EventNode<EntityEvent> node = EventNode.type("redstone-events", EventFilter.ENTITY);
		
		node.addChild(Buttons.events());
		node.addChild(Lever.events());
		node.addChild(Doors.events());
		node.addChild(Trapdoors.events());
		
		return node;
	}
	
	@Override
	public void initialize() {
		getEventNode().addChild(REDSTONE_EVENTS);
	}
	
	@Override
	public void terminate() {
	}
}
