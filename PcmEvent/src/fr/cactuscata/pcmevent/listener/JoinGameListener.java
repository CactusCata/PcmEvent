package fr.cactuscata.pcmevent.listener;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.cactuscata.pcmevent.PcmEvent;
import fr.cactuscata.pcmevent.command.simplecmd.inventoryviewver.OpenEnderCmd;
import fr.cactuscata.pcmevent.command.simplecmd.inventoryviewver.OpenInvCmd;
import fr.cactuscata.pcmevent.command.simplecmd.rank.RankList;
import fr.cactuscata.pcmevent.command.simplecmd.vanish.VanishCmd;
import fr.cactuscata.pcmevent.utils.bukkit.PlayerPcm;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.bukkit.SetTab;
import fr.cactuscata.pcmevent.utils.custominventory.InventoryHelper;

/**
 * <p>
 * Cette classe permet de remettre à zero les statistiques d'un joueur qui se
 * connecte, aussi, toutes les statistiques du joueurs comme son grade ainsi que
 * son experience dans dès qu'il se connecte.
 * </p>
 * <p>
 * Dès qu'un joueur se connecte il a un objet ({@link PlayerPcm}) qui lui est
 * propre. Tous les {@link PlayerPcm} sont stockés dans une {@link Map} avec
 * comme clef l'interface {@link Player} et comme valeur {@link PlayerPcm}.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.0.0
 * @see InventoryHelper Sql PlayerPcm
 */

public final class JoinGameListener implements Listener {

	private final InventoryHelper inv;

	/**
	 * Constructeur pour l'evenement {@link PlayerJoinEvent}.
	 * 
	 * @param inv
	 *            Pour enregistrer leurs inventaires pour la command
	 *            {@link OpenInvCmd} ainsi que {@link OpenEnderCmd}.
	 */
	public JoinGameListener(final InventoryHelper inv) {
		this.inv = inv;
	}

	/**
	 * Méthode écouteur de l'evenement {@link PlayerJoinEvent}.
	 * 
	 * @param event
	 *            L'event en question.
	 */
	@EventHandler
	public final void onJoin(final PlayerJoinEvent event) {

		event.setJoinMessage(null);
		final Player player = event.getPlayer();
		this.inv.setPlayerOnline(player);

		final PlayerPcm playerPcm = PlayerPcm.getPlayersPcm().containsKey(player)
				? PlayerPcm.getPlayersPcm().get(player)
				: new PlayerPcm(player);

		final RankList staff = playerPcm.getRank();

		if (!staff.isStaff())
			VanishCmd.getVanished().forEach(player::hidePlayer);

		SetTab.sendtab(player);

		player.setDisplayName(staff.getPrefix() + player.getName() + staff.getSuffix());
		player.sendMessage(
				PrefixMessage.PREFIX + "Bienvenue à toi " + player.getDisplayName() + "§e sur le monde event !");

		Bukkit.getScheduler().runTask(PcmEvent.getPlugin(), new Runnable() {

			@Override
			public final void run() {
				PlayerPcm.getPlayersPcm().values().forEach(PlayerPcm::updatePlayerNameTab);
			}
		});

		// Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
		//
		// @Override
		// public final void run() {
		// ExperienceClassementSystem.spawnNPC(JoinGameListener.this.plugin, player);
		// }
		// }, 30L);

	}

}
