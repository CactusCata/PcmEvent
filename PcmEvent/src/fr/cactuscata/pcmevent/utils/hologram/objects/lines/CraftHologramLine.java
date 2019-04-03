package fr.cactuscata.pcmevent.utils.hologram.objects.lines;

import org.bukkit.World;

import fr.cactuscata.pcmevent.utils.hologram.api.lines.HologramLine;
import fr.cactuscata.pcmevent.utils.hologram.objects.CraftHologram;

public abstract class CraftHologramLine implements HologramLine {
	private final double height;
	private final CraftHologram parent;
	private boolean isSpawned;

	protected CraftHologramLine(final double height, final CraftHologram parent) {
		this.height = height;
		this.parent = parent;
	}

	public final double getHeight() {
		return this.height;
	}

	public final CraftHologram getParent() {
		return this.parent;
	}

	public final void removeLine() {
		this.parent.removeLine(this);
	}

	public void spawn(World world, double x, double y, double z) {
		despawn();
		this.isSpawned = true;
	}

	public void despawn() {
		this.isSpawned = false;
	}

	public final boolean isSpawned() {
		return this.isSpawned;
	}

	public abstract int[] getEntitiesIDs();

	public abstract void teleport(final double paramDouble1, final double paramDouble2, final double paramDouble3);
}