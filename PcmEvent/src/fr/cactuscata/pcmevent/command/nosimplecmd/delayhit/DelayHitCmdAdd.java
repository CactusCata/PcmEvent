package fr.cactuscata.pcmevent.command.nosimplecmd.delayhit;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SubCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PlayerPcm;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;

/**
 * Cette classe permet le fonctionnement de l'argument <code>'add'</code> de la
 * commande <code>'/hitdelay'</code>.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.1
 * @see {@link DelayHitCmdRemove}, {@link DelayHitCmdReset},
 *      {@link DelayHitCmdSet}, {@link DelayHitCmd}.
 */

final class DelayHitCmdAdd extends SubCommand {

	/**
	 * Définis les valeurs par défaut pour sa super-classe {@link SubCommand}.
	 */
	DelayHitCmdAdd() {
		super("add", SenderType.ALL, "Veuillez préciser le joueur !", "Veuillez préciser la somme à ajouter !");
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final Player player = CommandValidator.getPlayerByString(args[0]);
		CommandValidator.isNotOnline(player, "Le joueur " + args[0] + " n'est pas connecté !");
		final int amount = CommandValidator.getInt(args[1]);
		CommandValidator.isSuperiorTo(amount, 2000);
		CommandValidator.isInferiorTo(player.getMaximumNoDamageTicks() + amount, 0);
		player.setMaximumNoDamageTicks(player.getMaximumNoDamageTicks() + amount);
		sender.sendMessage(String.format("%sVous avez ajouté %d ticks (Total: %d).", PrefixMessage.PREFIX, amount,
				player.getMaximumNoDamageTicks()));
	}

	@Override
	protected final String getHelp() {
		return "Grace à cet argument vous pouvez ajouter un delais en tick (maximum 2000 (100 secondes)).";
	}

	@Override
	protected final List<String> getSubArguments() {
		return PlayerPcm.getCorrectPlayersNames();
	}

}
