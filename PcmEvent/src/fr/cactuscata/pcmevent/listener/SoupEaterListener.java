package fr.cactuscata.pcmevent.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Cette cllasse permet faire fonctionner l'auto-soup.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.3.0
 * @see PlayerInteractEvent
 *
 */

public final class SoupEaterListener implements Listener {

	/**
	 * Méthode écouteur permettant de regenerer celui qui utilisera une soup.
	 * 
	 * @param event
	 *            L'event {@link PlayerInteractEvent}.
	 */
	@EventHandler
	public final void onEatSoup(final PlayerInteractEvent event) {

		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

			final ItemStack item = event.getItem();
			if (item == null || item.getType() != Material.MUSHROOM_SOUP)
				return;

			final Player player = event.getPlayer();
			if (player.getHealth() > 0.0d) {
				player.setHealth(player.getHealth() > 12.0d ? 20.0d : player.getHealth() + 8.0d);
				player.setItemInHand(new ItemStack(Material.AIR));
				event.setCancelled(true);
			}
		}
	}

}
