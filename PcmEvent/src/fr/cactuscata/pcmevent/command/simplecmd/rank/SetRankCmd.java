package fr.cactuscata.pcmevent.command.simplecmd.rank;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.command.simplecmd.vanish.VanishCmd;
import fr.cactuscata.pcmevent.utils.bukkit.PlayerPcm;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.bukkit.UUIDFetcher;
import fr.cactuscata.pcmevent.utils.sql.TableEventPlayersSql;
import fr.cactuscata.pcmevent.utils.sql.TableEventPlayersSql.SqlColumn;

/**
 * <p>
 * Cette classe permet d'executer la commande /setstaff, cette dernière permet
 * de mettre un {@link RankList grade} à joueur, connecté ou {@link UUIDFetcher
 * non}.
 * </p>
 * <p>
 * <strong>Il faut un mot de passe pour mettre un grade.</strong>
 * </p>
 * <p>
 * Met par ailleurs à jour le systeme de {@link VanishCmd}, c'est à dire si le
 * grade prend en considération la visibilité du {@link VanishCmd} via
 * {@link RankList#isStaff()}.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.0.0
 */

public final class SetRankCmd extends SimpleCommand {

	private final RankFile rankFile;
	
	/**
	 * Constructeur qui instancie la super classe.
	 */
	public SetRankCmd(final RankFile rankFile) {
		super(PrefixMessage.PREFIX, SenderType.ALL, "Veuillez préciser le joueur !", "Veuillez préciser le rank !",
				"Veuillez préciser le mot de passe !");
		this.rankFile = rankFile;
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final RankList rank = RankList.getRank(args[1]);
		CommandValidator.is(rank, RankList.NULL, "Le rank '" + args[1] + "' n'existe pas !");
		final Player player = CommandValidator.getPlayerByString(args[0]);
		CommandValidator.isNot(args[2], "auchan", "Mot de passe incorrect");

		if (CommandValidator.isOnline(player)) {

			final PlayerPcm playerPcm = PlayerPcm.getPlayersPcm().get(player);
			playerPcm.setRank(rank);

			if (sender instanceof Player)
				player.sendMessage(PrefixMessage.PREFIX + ((Player) sender).getDisplayName() + "§e vous a mis le grade "
						+ rank.getNameOfRank() + "§e !");
			else
				player.sendMessage(
						PrefixMessage.PREFIX + "La console vous a mis le grade " + rank.getNameOfRank() + "§e !");

			if (!rank.isStaff()) {
				if (VanishCmd.getVanished().contains(player)) {
					VanishCmd.getVanished().remove(player);
					Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.showPlayer(player));
				}
				VanishCmd.getVanished().forEach(vanishedStaff -> player.hidePlayer(vanishedStaff));
			} else
				VanishCmd.getVanished().forEach(vanishStaff -> player.showPlayer(vanishStaff));

			playerPcm.updatePlayerNameTab();

		} else {

			final UUID uuid = UUIDFetcher.getUUIDOf(args[0]);
			CommandValidator.isNull(uuid, PrefixMessage.PREFIX + "Le joueur n'existe pas !");
			final TableEventPlayersSql sqlInstance = TableEventPlayersSql.getTableEventInstance();
			CommandValidator.isTrue(!sqlInstance.haveAccount(uuid), "Le joueur '" + args[0] + "' ne c'est jamais connecté !");
			sqlInstance.updateStatsSql(uuid, SqlColumn.RANK, rank.getNameOfRank());

		}

		sender.sendMessage(PrefixMessage.PREFIX + "Vous avez mis le grade '" + rank.getNameOfRank() + "' au joueur "
				+ rank.getPrefix() + args[0] + rank.getSuffix() + "§e !");
		this.rankFile.write(sender.getName(), args[0], rank.getNameOfRank());
		

	}

	@Override
	protected final String getHelp() {
		return "Cette commande permet de mettre un rank au joueur précisé qu'il soit en ligne ou non.";
	}

	@Override
	protected final List<String> onTabComplete(final String[] args) {
		return args.length == 1 ? PlayerPcm.getCorrectPlayersNames()
				: args.length == 2
						? Arrays.asList(RankList.values()).stream().filter(staff -> staff != RankList.NULL)
								.map(staff -> staff.getNameOfRank()).collect(Collectors.toList())
						: Arrays.asList("");
	}
}
