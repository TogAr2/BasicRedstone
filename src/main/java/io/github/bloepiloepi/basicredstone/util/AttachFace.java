package io.github.bloepiloepi.basicredstone.util;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum AttachFace {
	FLOOR("floor"),
	WALL("wall"),
	CEILING("ceiling");
	
	private static final Map<String, AttachFace> BY_NAME = Arrays.stream(values())
			.collect(Collectors.toMap(AttachFace::getName, Function.identity()));
	
	public static AttachFace byName(String name) {
		return BY_NAME.get(name);
	}
	
	private final String name;
	
	AttachFace(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
