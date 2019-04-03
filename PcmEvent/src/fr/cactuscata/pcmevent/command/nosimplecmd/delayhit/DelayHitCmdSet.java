package fr.cactuscata.pcmevent.command.nosimplecmd.delayhit;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.utils.bukkit.PlayerPcm;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SubCommand;

/**
 * Cette classe permet d'executer le code de l'argument <code>'set'</code> de la
 * commande <code>'delayhit'</code>.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.1
 * @see {@link DelayHitCmd}, {@link DelayHitCmdAdd}, {@link DelayHitCmdRemove}
 *      {@link DelayHitCmdRemove}.
 */

final class DelayHitCmdSet extends SubCommand {

	/**
	 * Définis les valeurs par défaut pour sa super-classe {@link SubCommand}.
	 */
	DelayHitCmdSet() {
		super("set", SenderType.ALL, "Veuillez préciser le joueur !", "Veuillez préciser la somme à mettre !");
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final Player player = CommandValidator.getPlayerByString(args[0]);
		CommandValidator.isNotOnline(player, String.format("Le joueur '%s' n'est pas connecté !", args[0]));
		final int amount = CommandValidator.getInt(args[1]);
		CommandValidator.isBetweenTo(amount, 0, 2000);
		player.setMaximumNoDamageTicks(amount);
		sender.sendMessage(String.format("%sVous avez mis à %d ticks !", PrefixMessage.PREFIX, amount));
	}

	@Override
	protected final String getHelp() {
		return "Grace à cet argument vous pourrez fixer le delais entre lequel le joueur précisé pourra se faire frapper.";
	}

	@Override
	protected final List<String> getSubArguments() {
		return PlayerPcm.getCorrectPlayersNames();
	}

}
