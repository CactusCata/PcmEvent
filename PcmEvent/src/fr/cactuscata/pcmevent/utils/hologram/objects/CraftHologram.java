package fr.cactuscata.pcmevent.utils.hologram.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import fr.cactuscata.pcmevent.utils.hologram.api.Hologram;
import fr.cactuscata.pcmevent.utils.hologram.objects.lines.CraftHologramLine;
import fr.cactuscata.pcmevent.utils.hologram.objects.lines.CraftTextLine;
import fr.cactuscata.pcmevent.utils.other.Maths;

public class CraftHologram implements Hologram {
	private World world;
	private double x;
	private double y;
	private double z;
	private int chunkX;
	private int chunkZ;
	private final List<CraftHologramLine> lines;
	private boolean deleted;

	public CraftHologram(final Location location) {
		updateLocation(location.getWorld(), location.getX(), location.getY(), location.getZ());

		this.lines = new ArrayList<>();
	}

	public final boolean isInChunk(final Chunk chunk) {
		return (chunk.getX() == this.chunkX) && (chunk.getZ() == this.chunkZ);
	}

	public final World getWorld() {
		return this.world;
	}

	public final double getX() {
		return this.x;
	}

	public final double getY() {
		return this.y;
	}

	public final double getZ() {
		return this.z;
	}

	public final Location getLocation() {
		return new Location(this.world, this.x, this.y, this.z);
	}

	private final void updateLocation(final World world, final double x, final double y, final double z) {

		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.chunkX = (Maths.floor(x) >> 4);
		this.chunkZ = (Maths.floor(z) >> 4);
	}

	public final boolean isDeleted() {
		return this.deleted;
	}

	public void delete() {
		if (!this.deleted) {
			this.deleted = true;
			clearLines();
		}
	}

	public final List<CraftHologramLine> getLinesUnsafe() {
		return this.lines;
	}

	public final CraftHologramLine getLine(final int index) {
		return (CraftHologramLine) this.lines.get(index);
	}

	public final CraftTextLine appendTextLine(final String text) {

		CraftTextLine line = new CraftTextLine(this, text);
		this.lines.add(line);
		refreshSingleLines();
		return line;
	}

	public final CraftTextLine insertTextLine(final int index, final String text) {

		final CraftTextLine line = new CraftTextLine(this, text);
		this.lines.add(index, line);
		refreshSingleLines();
		return line;
	}

	public final void removeLine(final int index) {

		((CraftHologramLine) this.lines.remove(index)).despawn();
		refreshSingleLines();
	}

	public final void removeLine(final CraftHologramLine line) {

		this.lines.remove(line);
		line.despawn();
		refreshSingleLines();
	}

	public final void clearLines() {
		this.lines.forEach(line -> line.despawn());
		this.lines.clear();
	}

	public final int size() {
		return this.lines.size();
	}

	public final double getHeight() {
		if (this.lines.isEmpty()) {
			return 0.0D;
		}

		double height = 0.0D;

		for (final CraftHologramLine line : this.lines) {
			height += line.getHeight();
		}

		height += 0.2 * (this.lines.size() - 1);
		return height;
	}

	public final void refreshAll() {
		if (this.world.isChunkLoaded(this.chunkX, this.chunkZ)) {
			spawnEntities();
		}
	}

	public final void refreshSingleLines() {
		if (this.world.isChunkLoaded(this.chunkX, this.chunkZ)) {
			double currentY = this.y;
			boolean first = true;

			for (CraftHologramLine line : this.lines) {
				currentY -= line.getHeight();

				if (first) {
					first = false;
				} else {
					currentY -= 0.2;
				}

				if (line.isSpawned()) {
					line.teleport(this.x, currentY, this.z);
				} else {
					line.spawn(this.world, this.x, currentY, this.z);

				}
			}
		}
	}

	public final void spawnEntities() {

		despawnEntities();

		double currentY = this.y;
		boolean first = true;

		for (CraftHologramLine line : this.lines) {
			currentY -= line.getHeight();

			if (first) {
				first = false;
			} else {
				currentY -= 0.2;
			}

			line.spawn(this.world, this.x, currentY, this.z);

		}
	}

	public final void despawnEntities() {
		this.lines.forEach(line -> line.despawn());
	}

	public final void teleport(final Location location) {
		teleport(location.getWorld(), location.getX(), location.getY(), location.getZ());
	}

	public final void teleport(final World world, final double x, final double y, final double z) {
		updateLocation(world, x, y, z);

		if (this.world != world) {
			despawnEntities();
			refreshAll();
			return;
		}

		double currentY = y;
		boolean first = true;

		for (final CraftHologramLine line : this.lines) {
			if (line.isSpawned()) {

				currentY -= line.getHeight();

				if (first) {
					first = false;
				} else {
					currentY -= 0.2;
				}

				line.teleport(x, currentY, z);
			}
		}
	}

	public String toString() {
		return "CraftHologram [world=" + this.world + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", lines="
				+ this.lines + ", deleted=" + this.deleted + "]";
	}

}