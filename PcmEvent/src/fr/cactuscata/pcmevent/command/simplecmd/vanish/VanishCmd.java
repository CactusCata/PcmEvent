package fr.cactuscata.pcmevent.command.simplecmd.vanish;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.cactuscata.pcmevent.PcmEvent;
import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.command.simplecmd.rank.RankList;
import fr.cactuscata.pcmevent.utils.bukkit.PlayerPcm;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.bukkit.TitleManager;

/**
 * <p>
 * Cette classe permet l'éxecution de la commande /vanish, cette dernière permet
 * à un membre du staff du monde event d'être invisible, les personnes qui
 * pourront voir la personne en vanish est determiné via
 * {@link RankList#isStaff()}.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.3
 * @since 1.0.0
 */

public final class VanishCmd extends SimpleCommand {

	private static final ArrayList<Player> vanish = new ArrayList<>();

	/**
	 * Constructeur qui permet d'instancier la super-class.
	 * 
	 * @param plugin
	 *            Permet d'utiliser l'objet {@link BukkitRunnable} pour la boucle
	 *            avec le message.
	 */
	public VanishCmd() {
		super(PrefixMessage.VANISH, SenderType.PLAYER);
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final Player playerSender = (Player) sender;
		final PlayerPcm playerPcm = PlayerPcm.getPlayersPcm().get(playerSender);
		final RankList rank = playerPcm.getRank();

		if (vanish.contains(playerSender)) {

			Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.showPlayer(playerSender));

			super.sendMessage(playerSender, "Vous n'êtes plus en vanish");
			if (playerSender.getGameMode() == GameMode.SURVIVAL || playerSender.getGameMode() == GameMode.ADVENTURE)
				playerSender.setAllowFlight(false);
			playerSender.removePotionEffect(PotionEffectType.NIGHT_VISION);

			if (rank == RankList.ORGANISATEUR || rank == RankList.ORGANISATRICE)
				playerSender.setGameMode(GameMode.ADVENTURE);
			vanish.remove(playerSender);
			playerPcm.updatePlayerNameTab();

		} else {

			PlayerPcm.getPlayersPcm().values().stream().filter(oPlayerPcm -> !oPlayerPcm.getRank().isStaff())
					.forEach(oPlayerPcm -> oPlayerPcm.getPlayer().hidePlayer(playerSender));

			if (rank == RankList.ORGANISATEUR || rank == RankList.ORGANISATRICE)
				playerSender.setGameMode(GameMode.SURVIVAL);

			playerSender.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 0, true));
			playerSender.setAllowFlight(true);
			vanish.add(playerSender);
			playerPcm.updatePlayerNameTab();
			this.actionMessageVanish(playerSender);
			super.sendMessage(playerSender, "Vous êtes maintenant en vanish");
		}
	}

	/**
	 * Méthode qui revois la liste des joueurs qui ont executés la commande
	 * <code>'/vanish'</code>.
	 * 
	 * @return La liste des joueurs qui ont executés la commande
	 *         <code>'/vanish'</code>.
	 */
	public static final ArrayList<Player> getVanished() {
		return VanishCmd.vanish;
	}

	/**
	 * Méthode qui envera en boucle un message dans l'action bar.
	 * 
	 * @param playersender
	 *            Joueur en vanish qui recevera le message en boucle.
	 */
	private final void actionMessageVanish(final Player playerSender) {
		
		new BukkitRunnable() {

			private final String corps = "    Vous êtes maintenant en Vanish !    ";
			private int valuemin = 4, valuemax = 22;
			private final int size = corps.length();

			@Override
			public final void run() {

				if (!vanish.contains(playerSender))
					super.cancel();

				if (valuemin == size)
					valuemin = 0;

				if (valuemax == size)
					valuemax = 0;

				valuemin++;
				valuemax++;

				TitleManager.sendActionBarMessage(playerSender,
						String.format("{\"text\": \"%s\"}", (valuemin < valuemax
								? corps.substring(valuemin - 1, valuemax - 1)
								: corps.substring(valuemin - 1, this.size) + corps.substring(0, valuemax - 1))));

			}
		}.runTaskTimer(PcmEvent.getPlugin(), 0L, 40L);
	}

	@Override
	protected final String getHelp() {
		return "Grace à cette commande vous pourrez vous cacher des joueurs.";
	}

}
