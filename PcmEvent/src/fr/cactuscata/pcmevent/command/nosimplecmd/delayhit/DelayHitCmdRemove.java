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
 * Cette commande permet le fonctionnement de l'argument <code>'remove'</code>
 * de la commande <code>'hitdelay'</code>.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.1
 * @see {@link DelayHitCmd}, {@link DelayHitCmdAdd}, {@link DelayHitCmdReset},
 *      {@link DelayHitCmdSet}.
 */

final class DelayHitCmdRemove extends SubCommand {

	/**
	 * D�finis les valeurs par d�faut pour sa super-classe {@link SubCommand}.
	 */
	DelayHitCmdRemove() {
		super("remove", SenderType.ALL, "Veuillez pr�ciser le joueur !", "Veuillez pr�ciser la somme � retirer !");
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final Player player = CommandValidator.getPlayerByString(args[0]);
		CommandValidator.isNotOnline(player, "Le joueur " + args[0] + " n'est pas connect� !");
		final int amount = CommandValidator.getInt(args[1]);
		CommandValidator.isInferiorTo(amount, 0);
		CommandValidator.isInferiorTo(player.getMaximumNoDamageTicks() - amount, 0);
		player.setMaximumNoDamageTicks(player.getMaximumNoDamageTicks() - amount);
		sender.sendMessage(String.format("%sVous avez retir� %d ticks (Total: %d).", PrefixMessage.PREFIX, amount,
				player.getMaximumNoDamageTicks()));
	}

	@Override
	protected final String getHelp() {
		return "Grace � cet argument vous pouvez retirer le delais entre lequel le joueur souhait� pourra �tre touch�. ";
	}

	@Override
	protected final List<String> getSubArguments() {
		return PlayerPcm.getCorrectPlayersNames();
	}

}
