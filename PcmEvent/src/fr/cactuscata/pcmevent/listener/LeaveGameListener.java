package fr.cactuscata.pcmevent.listener;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import fr.cactuscata.pcmevent.command.simplecmd.GamemodebCmd;
import fr.cactuscata.pcmevent.command.simplecmd.inventoryviewver.OpenEnderCmd;
import fr.cactuscata.pcmevent.command.simplecmd.inventoryviewver.OpenInvCmd;
import fr.cactuscata.pcmevent.command.simplecmd.spy.MsgCmd;
import fr.cactuscata.pcmevent.command.simplecmd.spy.SocialSpyCmd;
import fr.cactuscata.pcmevent.command.simplecmd.vanish.VanishCmd;
import fr.cactuscata.pcmevent.utils.bukkit.PlayerPcm;
import fr.cactuscata.pcmevent.utils.custominventory.InventoryHelper;
import fr.cactuscata.pcmevent.utils.sql.Sql;

/**
 * <p>
 * Cette classe permet de gerer l'evenement {@link PlayerQuitEvent}, ce dernier
 * permettra de retirer des {@link List} du {@link SocialSpyCmd},
 * {@link MsgCmd}, {@link VanishCmd} et/ou {@link GamemodebCmd} le joueur. Ce
 * dernier verra ses statistiques se mettres à jour dans la {@link Sql base de
 * donnée} et retiré de la {@link Map} de l'objet {@link PlayerPcm}.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.3
 * @since 1.0.0
 * @see InventoryHelper PlayerPcm
 */

public final class LeaveGameListener implements Listener {

	private final InventoryHelper inv;

	/**
	 * Constructeur qui passe en paramètre {@link InventoryHelper}.
	 * 
	 * @param inv
	 *            Pour enregistrer leurs inventaires pour la command
	 *            {@link OpenInvCmd} ainsi que {@link OpenEnderCmd}.
	 */
	public LeaveGameListener(final InventoryHelper inv) {
		this.inv = inv;
	}

	/**
	 * Méthode évouteur de l'evenement {@link PlayerQuitEvent}.
	 * 
	 * @param event
	 *            L'evenement ecouteur cible.
	 */
	@EventHandler
	public final void onQuit(final PlayerQuitEvent event) {

		event.setQuitMessage(null);

		final Player player = event.getPlayer();
		this.inv.setPlayerOffline(player);

		if (SocialSpyCmd.getSocial().contains(player))
			SocialSpyCmd.getSocial().remove(player);

		final Map<CommandSender, CommandSender> mapAnswer = MsgCmd.getAnswerMap();

		if (mapAnswer.containsKey(player) || mapAnswer.containsValue(player))
			mapAnswer.remove(player);

		if (VanishCmd.getVanished().contains(player)) {
			Bukkit.getOnlinePlayers().forEach(playerOnline -> playerOnline.showPlayer(player));
			VanishCmd.getVanished().remove(player);
			player.removePotionEffect(PotionEffectType.NIGHT_VISION);
		}

		if (GamemodebCmd.getGamemodeB().contains(player))
			GamemodebCmd.getGamemodeB().remove(player);

		PlayerPcm.getPlayersPcm().get(player).saveInSql(true);

		if (!player.isOp()) {

			player.teleport(new Location(Bukkit.getWorlds().get(0), -0.5d, 65.5d, -0.5d, 0.0f, 10.0f));
			player.setGameMode(GameMode.ADVENTURE);
			player.setLevel(0);
			player.setExp(0.0f);
			player.setFoodLevel(20);
			player.setHealth(20.0d);
			player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
			player.getInventory().clear();
			player.getInventory().setArmorContents(new ItemStack[] { null, null, null, null });
			player.updateInventory();
			player.setWalkSpeed(0.2f);
			player.setFlySpeed(0.1f);

		}

	}

}
