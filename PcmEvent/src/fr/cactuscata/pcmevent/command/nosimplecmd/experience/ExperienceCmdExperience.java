package fr.cactuscata.pcmevent.command.nosimplecmd.experience;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SubCommand;
import fr.cactuscata.pcmevent.command.simplecmd.rank.RankList;
import fr.cactuscata.pcmevent.utils.bukkit.PlayerPcm;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.experience.Experience;
import fr.cactuscata.pcmevent.utils.experience.ExperienceManager;
import fr.cactuscata.pcmevent.utils.sql.TableEventPlayersSql;
import fr.cactuscata.pcmevent.utils.sql.TableEventPlayersSql.SqlColumn;

/**
 * Cette classe permet de gerer l'argument <code>'experience'</code> pour
 * modifier cette dernière au joueur précisé. Il est possible de cibler un
 * joueur déconnecté.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.4.0
 * @see {@link ExperienceCmd}, {@link ExperienceCmdLevel},
 *      {@link ExperienceCmdUpdate}.
 */

final class ExperienceCmdExperience extends SubCommand {

	/**
	 * Définis les valeurs par défaut pour sa super-classe {@link SubCommand}.
	 */
	ExperienceCmdExperience() {
		super("experience", SenderType.ALL, "Veuillez préciser add/remove/set/reset !",
				"Veuillez préciser le joueur !");
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final Player player = CommandValidator.getPlayerByString(args[1]);
		final boolean isCorrectPlayer = CommandValidator.isOnline(player);
		final UUID uuid = isCorrectPlayer ? null : CommandValidator.getUUIDByName(args[1]);

		CommandValidator.isTrue(
				(isCorrectPlayer && !PlayerPcm.getPlayersPcm().get(player).getRank().isAcceptXP())
						|| (!isCorrectPlayer && !RankList
								.getRank(String.valueOf(
										TableEventPlayersSql.getTableEventInstance().get(uuid, SqlColumn.RANK)))
								.isAcceptXP()),
				"Les membres du staff du monde event ne peuvent pas recevoir d'experience !");

		final Experience experience = isCorrectPlayer ? PlayerPcm.getPlayersPcm().get(player).getExperience() : null;
		final String playerName = isCorrectPlayer ? player.getDisplayName() : args[1];

		if (args[0].equalsIgnoreCase("reset")) {
			if (isCorrectPlayer)
				experience.resetExperience();
			else
				ExperienceManager.resetExperienceOfflinePlayer(uuid);
			sender.sendMessage(
					PrefixMessage.PREFIX + "Vous avez remis à zéro les niveaux du joueur " + playerName + ".");
			return;
		}

		CommandValidator.argsSizeIsPresent(args, 3, "Veuilez préciser la valeur !");
		final int amount = CommandValidator.getInt(args[2]);
		CommandValidator.isBetweenTo(amount, 0, Integer.MAX_VALUE);

		if (args[0].equalsIgnoreCase("add")) {
			if (isCorrectPlayer)
				experience.addExperience(amount);
			else
				ExperienceManager.addExperienceOffinePlayer(uuid, amount);
		} else if (args[0].equalsIgnoreCase("remove")) {
			if (isCorrectPlayer)
				experience.removeExperience(amount);
			else
				ExperienceManager.removeExperienceOfflinePlayer(uuid, amount);
		} else if (args[0].equalsIgnoreCase("set")) {
			if (isCorrectPlayer)
				experience.setExperience(amount);
			else
				ExperienceManager.setExperienceOfflinePlayer(uuid, amount);
		} else
			throw new CommandException(
					String.format("L'argument '%s' n'est pas valide, essayez %s !", args[0], this.getSubArguments()));

		sender.sendMessage(String.format("%s Vous avez %s %d experience au joueur %s", PrefixMessage.PREFIX,
				args[0].toLowerCase(), amount, playerName));
	}

	@Override
	protected final String getHelp() {
		return "Grace a cet argument vous pourrez modifier l'experience du joueur précisé.";
	}

	@Override
	protected final List<String> getSubArguments() {
		return Arrays.asList("reset", "set", "add", "remove");
	}

}
