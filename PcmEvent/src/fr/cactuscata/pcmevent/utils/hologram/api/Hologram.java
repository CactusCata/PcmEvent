package fr.cactuscata.pcmevent.utils.hologram.api;

import org.bukkit.Location;
import org.bukkit.World;

import fr.cactuscata.pcmevent.utils.hologram.api.lines.HologramLine;
import fr.cactuscata.pcmevent.utils.hologram.api.lines.TextLine;

public abstract interface Hologram {
	public abstract TextLine appendTextLine(String paramString);

	public abstract TextLine insertTextLine(int paramInt, String paramString);

	public abstract HologramLine getLine(int paramInt);

	public abstract void removeLine(int paramInt);

	public abstract void clearLines();

	public abstract int size();

	public abstract double getHeight();

	public abstract void teleport(Location paramLocation);

	public abstract void teleport(World paramWorld, double paramDouble1, double paramDouble2, double paramDouble3);

	public abstract Location getLocation();

	public abstract double getX();

	public abstract double getY();

	public abstract double getZ();

	public abstract World getWorld();

	public abstract void delete();

	public abstract boolean isDeleted();
}
