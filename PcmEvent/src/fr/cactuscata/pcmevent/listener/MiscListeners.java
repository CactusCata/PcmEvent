package fr.cactuscata.pcmevent.listener;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import fr.cactuscata.pcmevent.command.nosimplecmd.physic.PhysicFile;
import fr.cactuscata.pcmevent.command.simplecmd.inventoryviewver.OpenEnderCmd;
import fr.cactuscata.pcmevent.command.simplecmd.inventoryviewver.OpenInvCmd;
import fr.cactuscata.pcmevent.utils.custominventory.InventoryHelper;

/**
 * <p>
 * Cette classe permet de gerer trois {@link Event evenements} de bukkit,
 * {@link WeatherChangeEvent} ainsi que {@link PlayerChangedWorldEvent} et
 * {@link BlockPhysicsEvent}. Le premier est présent pour arreter la pluie si
 * elle survient et le second pour mettre à jour l'objet {@link InventoryHelper}
 * et le derniere et là pour désactiver la physiqye du monde via l'objet
 * {@link PhysicFile} .
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.0.0
 * @see InventoryHelper WeatherChangeEvent PlayerChangedWorldEvent PhysicFile
 */

public final class MiscListeners implements Listener {

	private final InventoryHelper inv;
	private final PhysicFile physicFile;

	/**
	 * Constructeur de la classe.
	 * 
	 * @param inv
	 *            Pour enregistrer leurs inventaires pour la command
	 *            {@link OpenInvCmd} ainsi que {@link OpenEnderCmd}.
	 * @param physicFile
	 *            Pour gerer l'evenement {@link BlockPhysicsEvent}.
	 */
	public MiscListeners(final InventoryHelper inv, PhysicFile physicFile) {
		this.inv = inv;
		this.physicFile = physicFile;
	}

	/**
	 * Méthode écouteur {@link WeatherChangeEvent} qui permettra de rétablir le beau
	 * temps dans le monde.
	 * 
	 * @param event
	 *            Event {@link WeatherChangeEvent}.
	 */
	@EventHandler
	public final void weatherChange(final WeatherChangeEvent event) {
		event.setCancelled(true);
	}

	/**
	 * Méthode écouteur {@link PlayerChangedWorldEvent} qui permet de mettre à jour
	 * l'inventaire avec
	 * {@link InventoryHelper#changeWorld(org.bukkit.entity.Player)}.
	 * 
	 * @param event
	 *            Event {@link PlayerChangedWorldEvent}.
	 */
	@EventHandler
	public final void changeWorld(final PlayerChangedWorldEvent event) {
		this.inv.changeWorld(event.getPlayer());
	}

	/**
	 * Mothode écouteur {@link BlockPhysicsEvent} qui permettra de desactiver la
	 * physique de certains blocks.
	 * 
	 * @param event
	 *            Event {@link BlockPhysicsEvent}.
	 */
	@EventHandler
	public final void usePhysic(final BlockPhysicsEvent event) {
		if (!this.physicFile.isPhysicEnable() && this.physicFile.getList().contains(event.getBlock().getType()))
			event.setCancelled(true);
	}

}
