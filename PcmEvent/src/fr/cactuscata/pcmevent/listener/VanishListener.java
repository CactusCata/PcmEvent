package fr.cactuscata.pcmevent.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import fr.cactuscata.pcmevent.PcmEvent;
import fr.cactuscata.pcmevent.command.nosimplecmd.config.ConfigCmd;
import fr.cactuscata.pcmevent.command.simplecmd.GamemodebCmd;
import fr.cactuscata.pcmevent.command.simplecmd.vanish.VanishCmd;
import fr.cactuscata.pcmevent.command.simplecmd.vanish.VanishFile;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;

/**
 * <p>
 * Cette classe permet de gérer une partie des {@link Event evenements de
 * bukkit}, c'est à dire ici : {@link PlayerDropItemEvent},
 * {@link EntityDamageByEntityEvent}, {@link EntityDamageEvent},
 * {@link FoodLevelChangeEvent}, {@link PlayerPickupItemEvent},
 * {@link BlockCanBuildEvent}, {@link BlockBreakEvent}, {@link BlockPlaceEvent}
 * et {@link PlayerExpChangeEvent}
 * </p>
 * <p>
 * Ils sont gérés par la méthode
 * {@link VanishListener#configManager(Player, String, String)} ou
 * {@link VanishListener#configManager(Player, String, String, Event)} si l'on
 * veut que l'evenement sois {@link Cancellable#setCancelled(boolean)}.
 * </p>
 * <p>
 * Toutes les evenements qui touchent au vanish récupère la valeur de chaque
 * adresse dans l'objet {@link VanishFile}.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.3
 * @since 1.0.0
 * @see ConfigCmd
 */

public class VanishListener implements Listener {

	private final List<Player> messageTimed = new ArrayList<>();
	private final VanishFile vanishFile;
	private final Plugin plugin = PcmEvent.getPlugin();

	/**
	 * Constructeur qui faot passer en paramètre {@link VanishFile}.
	 * 
	 * @param vanishFile
	 *            Permettra de verifier
	 */
	public VanishListener(final VanishFile vanishFile) {
		this.vanishFile = vanishFile;
	}

	/**
	 * Méthode écouteur de l'evenement {@link FoodLevelChangeEvent} qui permettra
	 * d'annuler la perte de saturation.
	 * 
	 * @param event
	 *            Event {@link FoodLevelChangeEvent}.
	 */
	@EventHandler
	public final void onFoodChange(final FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player && (VanishCmd.getVanished().contains((Player) event.getEntity())
				|| GamemodebCmd.getGamemodeB().contains((Player) event.getEntity())))
			event.setCancelled(true);

	}

	/**
	 * Méthode écouteur de l'evenement {@link PlayerPickupItemEvent} qui pemettra
	 * d'annuler la récuperation d'items au sol.
	 * 
	 * @param event
	 *            L'event {@link PlayerPickupItemEvent}.
	 */
	@EventHandler
	public final void onPickupItem(final PlayerPickupItemEvent event) {
		if (configManager(event.getPlayer(), "CanPickupItems", "Vous ne pouvez pas récupérer d'items en vanish !")) {
			event.getItem().setPickupDelay(20);
			event.setCancelled(true);

		}

	}

	/**
	 * Méthode écouteur de l'evenement {@link BlockCanBuildEvent} qui pemettra de
	 * confirmer la pose de bloc sur un joueur.
	 * 
	 * @param event
	 *            L'event {@link BlockCanBuildEvent}.
	 */
	@EventHandler
	public final void onBlockCanBuild(final BlockCanBuildEvent event) {
		final List<Player> playerList = event.getBlock().getWorld().getPlayers();
		final Location loc = event.getBlock().getLocation();
		for (final Player player : playerList) {
			if (VanishCmd.getVanished().contains(player) && player.getLocation().distanceSquared(loc) <= 2.0D)
				event.setBuildable(true);
		}
	}

	/**
	 * Méthode écouteur de l'evenement {@link EntityDamageByEntityEvent} qui
	 * annulera les dégats.
	 * 
	 * Il y a aussi le fonctionnement de la flèche qui ignorera un joueur en vanish.
	 * 
	 * @param event
	 *            L'event {@link EntityDamageByEntityEvent}.
	 */
	@EventHandler
	public final void onTakeDamageByEntity(final EntityDamageByEntityEvent event) {
		// senddmgplayer event.getDamager();
		// getdmgplayer event.getEntity();

		final Entity entityDamager = event.getDamager(), entityDamaged = event.getEntity();

		if (entityDamager instanceof Player) {
			final Player sendDamager = (Player) entityDamager;
			configManager(sendDamager, "CanHitPlayersWithVanish",
					"Vous ne pouvez pas frapper les gens tout en étant en vanish !", event);

			if (GamemodebCmd.getGamemodeB().contains(entityDamaged)) {
				sendDamager.sendMessage(PrefixMessage.PREFIX + "Vous ne pouvez pas frapper les Organisateurs !");
				event.setCancelled(true);
			}
		} else if (entityDamager instanceof Arrow) {
			final Arrow arrow = (Arrow) entityDamager;
			if ((entityDamaged instanceof Player) && (arrow.getShooter() instanceof Player)) {
				final Player damaged = (Player) entityDamaged;
				if (VanishCmd.getVanished().contains(damaged)) {
					final Vector velocity = arrow.getVelocity();
					damaged.teleport(damaged.getLocation().add(0.0D, 2.0D, 0.0D));
					final Arrow nextArrow = (Arrow) arrow.getShooter().launchProjectile(Arrow.class);
					nextArrow.setVelocity(velocity);
					nextArrow.setBounce(false);
					nextArrow.setShooter(arrow.getShooter());
					nextArrow.setFireTicks(arrow.getFireTicks());
					nextArrow.setCritical(arrow.isCritical());
					nextArrow.setKnockbackStrength(arrow.getKnockbackStrength());
					event.setCancelled(true);
					arrow.remove();
				}
			}
		}

	}

	/**
	 * Méthode écouteur de l'evenement {@link EntityDamageEvent} qui annule les
	 * dégats si l'entité est en vanish ou a executé précédemment la commande
	 * <code>/gamemodeb</code>.
	 * 
	 * @param event
	 *            L'event {@link EntityDamageEvent}.
	 */
	@EventHandler
	public final void onDamage(final EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		final Player player = (Player) event.getEntity();
		if (VanishCmd.getVanished().contains(player) || GamemodebCmd.getGamemodeB().contains(player))
			event.setCancelled(true);
	}

	/**
	 * Méthode écouteur de l'evenement {@link BlockBreakEvent} qui annule la cassage
	 * de bloc si le joueur est en vanish.
	 * 
	 * @param event
	 *            L'event {@link BlockBreakEvent}.
	 */
	@EventHandler
	public final void onBlockBreak(final BlockBreakEvent event) {
		configManager(event.getPlayer(), "CanBreakBlock", "Vous ne pouvez pas casser de block en Vanish !", event);
	}

	/**
	 * Méthode écouteur de l'evenement {@link BlockPlaceEvent} qui annule le
	 * placemenent de bloc si le joueur est en vanish.
	 * 
	 * @param event
	 *            L'event {@link BlockPlaceEvent}.
	 */
	@EventHandler
	public final void onBlockPlace(final BlockPlaceEvent event) {
		configManager(event.getPlayer(), "CanPlaceBlock", "Vous ne pouvez pas placer de block en Vanish !", event);
	}

	/**
	 * Méthode écouteur de l'evenement {@link PlayerExpChangeEvent} qui annule la
	 * récuperation d'experience si le joueur est en vanish.
	 * 
	 * @param event
	 *            L'event {@link PlayerExpChangeEvent}.
	 */
	@EventHandler
	public final void onExperienceChange(final PlayerExpChangeEvent event) {
		if (configManager(event.getPlayer(), "AllowXP", "Vous ne pouvez pas récuperer l'xp au sol !"))
			event.setAmount(0);
	}

	/**
	 * Méthode écouteur de l'evenement {@link PlayerDropItemEvent} qui annule le
	 * drop d'item si le joueur est en vanish.
	 * 
	 * @param event
	 *            L'event {@link PlayerDropItemEvent}.
	 */
	@EventHandler
	public final void onDrop(final PlayerDropItemEvent event) {
		configManager(event.getPlayer(), "CanDropItems", "Vous ne pouvez rien drop en vanish !", event);
	}

	/**
	 * Méthode qui permet de vérifier si le joueur est bien en vanish et que la
	 * config respecte le path.
	 * 
	 * @param player
	 *            Joueur cible.
	 * @param adresse
	 *            Adresse dans la config.
	 * @param message
	 *            Message d'erreur.
	 * @return <code>true</code> si la path est répond <code>true</code>.
	 */
	private final boolean configManager(final Player player, final String adresse, final String message) {
		if (VanishCmd.getVanished().contains(player) && !this.vanishFile.getValue(adresse)) {
			if (!this.messageTimed.contains(player)) {
				player.sendMessage(PrefixMessage.VANISH + message);
				this.messageTimed.add(player);
				Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable() {
					@Override
					public final void run() {
						VanishListener.this.messageTimed.remove(player);
					}
				}, 40L);
			}
			return true;
		}
		return false;
	}

	/**
	 * Méthode similaire à
	 * {@link VanishListener#configManager(Player, String, String)}.
	 * 
	 * @param player
	 *            Joueur cible.
	 * @param adresse
	 *            Adresse dans la config.
	 * @param message
	 *            Message d'erreur.
	 * @return <code>true</code> si la path est répond <code>true</code>.
	 */
	private final void configManager(final Player player, final String adresse, final String message,
			final Event event) {

		if (this.configManager(player, adresse, message))
			((Cancellable) event).setCancelled(true);

	}

}
