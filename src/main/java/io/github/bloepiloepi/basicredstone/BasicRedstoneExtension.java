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
	
	/**
	 * Creates an EventNode with all the builtin redstone events listening.
	 *
	 * @return The EventNode
	 */
	public static EventNode<EntityEvent> events() {
		EventNode<EntityEvent> node = EventNode.type("redstone-events", EventFilter.ENTITY);
		
		node.addChild(buttonEvents());
		node.addChild(leverEvents());
		node.addChild(doorEvents());
		node.addChild(trapdoorEvents());
		
		return node;
	}
	
	/**
	 * Creates an EventNode with the events for button pressing and breaking listening.
	 *
	 * @return The EventNode
	 */
	public static EventNode<EntityEvent> buttonEvents() {
		return Buttons.events();
	}
	
	/**
	 * Creates an EventNode with the events for lever pulling and breaking listening.
	 *
	 * @return The EventNode
	 */
	public static EventNode<EntityEvent> leverEvents() {
		return Lever.events();
	}
	
	/**
	 * Creates an EventNode with the events for manual (no redstone) door interaction listening.
	 *
	 * @return The EventNode
	 */
	public static EventNode<EntityEvent> doorEvents() {
		return Doors.events();
	}
	
	/**
	 * Creates an EventNode with the events for manual (no redstone) trapdoor interaction listening.
	 *
	 * @return The EventNode
	 */
	public static EventNode<EntityEvent> trapdoorEvents() {
		return Trapdoors.events();
	}
	
	@Override
	public void initialize() {
	
	}
	
	@Override
	public void terminate() {
	
	}
}
