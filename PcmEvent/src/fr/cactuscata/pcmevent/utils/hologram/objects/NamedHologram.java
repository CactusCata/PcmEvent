package fr.cactuscata.pcmevent.utils.hologram.objects;

import org.bukkit.Location;

public final class NamedHologram extends CraftHologram {
	private final String name;

	public NamedHologram(final Location source, final String name) {
		super(source);
		this.name = name;
	}

	public final String getName() {
		return this.name;
	}

	public final void delete() {
		super.delete();
		NamedHologramManager.removeHologram(this);
	}

	public final String toString() {
		return "NamedHologram [name=" + this.name + ", super=" + super.toString() + "]";
	}
}
