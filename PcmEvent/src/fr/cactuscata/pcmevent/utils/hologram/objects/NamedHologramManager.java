package fr.cactuscata.pcmevent.utils.hologram.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;

public final class NamedHologramManager {
	private static final List<NamedHologram> pluginHolograms = new ArrayList<>();

	public static final void addHologram(final NamedHologram hologram) {
		pluginHolograms.add(hologram);
	}

	public static final void removeHologram(final NamedHologram hologram) {
		pluginHolograms.remove(hologram);
		if (!hologram.isDeleted()) {
			hologram.delete();
		}
	}

	public static final List<NamedHologram> getHolograms() {
		return new ArrayList<>(pluginHolograms);
	}
	
	public static final List<String> getHologramsNames() {
		List<String> holosNames = new ArrayList<>();
		pluginHolograms.forEach(holo -> holosNames.add(holo.getName()));
		return holosNames;
	}

	public static final NamedHologram getHologram(final String name) {
		for (NamedHologram hologram : pluginHolograms) {
			if (hologram.getName().equals(name)) {
				return hologram;
			}
		}
		return null;
	}

	public static final boolean isExistingHologram(final String name) {
		return getHologram(name) != null;
	}

	public static final void onChunkLoad(final Chunk chunk) {
		for (NamedHologram hologram : pluginHolograms) {
			if (hologram.isInChunk(chunk)) {
				hologram.spawnEntities();
			}
		}
	}

	public static final void onChunkUnload(final Chunk chunk) {
		for (NamedHologram hologram : pluginHolograms) {
			if (hologram.isInChunk(chunk)) {
				hologram.despawnEntities();
			}
		}
	}

	public static final void clearAll() {
		final List<NamedHologram> oldHolograms = new ArrayList<>(pluginHolograms);
		pluginHolograms.clear();

		for (NamedHologram hologram : oldHolograms) {
			hologram.delete();
		}
	}

	public static final int size() {
		return pluginHolograms.size();
	}

	public static final NamedHologram get(final int i) {
		return (NamedHologram) pluginHolograms.get(i);
	}
}