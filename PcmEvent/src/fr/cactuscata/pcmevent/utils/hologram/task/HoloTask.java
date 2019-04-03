package fr.cactuscata.pcmevent.utils.hologram.task;

import fr.cactuscata.pcmevent.utils.hologram.objects.NamedHologramManager;

public class HoloTask implements Runnable {
	public void run() {
		NamedHologramManager.getHolograms().forEach(holo -> holo.refreshAll());
	}
}
