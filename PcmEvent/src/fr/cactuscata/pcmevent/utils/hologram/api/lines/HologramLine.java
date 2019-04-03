package fr.cactuscata.pcmevent.utils.hologram.api.lines;

import fr.cactuscata.pcmevent.utils.hologram.api.Hologram;

public abstract interface HologramLine {
	public abstract Hologram getParent();

	public abstract void removeLine();
}
