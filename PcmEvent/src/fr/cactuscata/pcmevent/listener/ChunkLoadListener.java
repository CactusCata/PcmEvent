package fr.cactuscata.pcmevent.listener;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.Plugin;

import fr.cactuscata.pcmevent.PcmEvent;
import fr.cactuscata.pcmevent.utils.hologram.api.Hologram;
import fr.cactuscata.pcmevent.utils.hologram.objects.NamedHologramManager;

/**
 * Cette classe permet d'afficher aux joueurs présents l'hologramme que chaque
 * chunk contient.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see Hologram
 */

public final class ChunkLoadListener implements Listener {

	private final Plugin plugin = PcmEvent.getPlugin();
	
	/**
	 * Ecouteur de l'evenement {@link ChunkLoadEvent}.
	 * 
	 * @param event
	 *            Evenement.
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onChunkLoad(ChunkLoadEvent event) {

		final Chunk chunk = event.getChunk();

		if (chunk.isLoaded()) {

			if (Bukkit.isPrimaryThread())
				NamedHologramManager.onChunkLoad(chunk);
			else
				Bukkit.getScheduler().runTask(this.plugin, new Runnable() {
					@Override
					public final void run() {
						NamedHologramManager.onChunkLoad(chunk);
					}
				});

		}
	}

}
