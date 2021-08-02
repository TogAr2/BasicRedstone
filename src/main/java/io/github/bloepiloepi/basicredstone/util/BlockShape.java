package io.github.bloepiloepi.basicredstone.util;

import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;

public class BlockShape {
	private final Point offset;
	private final Point dimensions;
	
	public BlockShape(Point offset, Point dimensions) {
		this.offset = offset;
		this.dimensions = dimensions;
	}
	
	public BlockShape(double offsetX, double offsetY, double offsetZ, double width, double height, double depth) {
		this.offset = new Vec(offsetX, offsetY, offsetZ);
		this.dimensions = new Vec(width, height, depth);
	}
	
	public Point getOffset() {
		return offset;
	}
	
	public Point getDimensions() {
		return dimensions;
	}
	
	public BlockShape applyOffset(Point offset) {
		return new BlockShape(this.offset.add(offset), dimensions);
	}
	
	public boolean contains(Entity entity) {
		BoundingBox boundingBox = entity.getBoundingBox();
		
		return (offset.x() <= boundingBox.getMaxX() && offset.x() + dimensions.x() >= boundingBox.getMinX()) &&
				(offset.y() <= boundingBox.getMaxY() && offset.y() + dimensions.y() >= boundingBox.getMinY()) &&
				(offset.z() <= boundingBox.getMaxZ() && offset.z() + dimensions.z() >= boundingBox.getMinZ());
	}
}
