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
 * Cette classe permet le fonctionnement de l'argument <code>'reset'</code> de
 * la commande <code>'delayhit'</code>.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.1
 * @see {@link DelayHitCmd}, {@link DelayHitCmdAdd}, {@link DelayHitCmdRemove},
 *      {@link DelayHitCmdSet}.
 */

final class DelayHitCmdReset extends SubCommand {

	/**
	 * Définis les valeurs par défaut pour sa super-classe {@link SubCommand}.
	 */
	DelayHitCmdReset() {
		super("reset", SenderType.ALL, "Veuillez préciser le joueur !");
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final Player player = CommandValidator.getPlayerByString(args[0]);
		CommandValidator.isNotOnline(player, "Le joueur '" + args[0] + "' n'est pas connecté !");
		player.setMaximumNoDamageTicks(20);
		sender.sendMessage(String.format("%sVous avez remis à 20 les ticks par défaut.", PrefixMessage.PREFIX));
	}

	@Override
	protected final String getHelp() {
		return "Grace à cet argument vous pourrez le délais par défaut du temps entre lequel le joueur peut se prendre un coup.";
	}

	@Override
	protected final List<String> getSubArguments() {
		return PlayerPcm.getCorrectPlayersNames();
	}

}
