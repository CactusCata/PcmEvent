package fr.cactuscata.pcmevent.command.simplecmd.tp;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PlayerPcm;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;

/**
 * <p>
 * Cette classe permet l'execution de la commande /tpb, cette derni�re est
 * principalement faite pour les organisateurs. Elle permet de t�l�porter un
 * joueur � un autre.
 * </p>
 * 
 * @author CactusCata
 * @version 2.4.1
 * @since 1.0.0
 */

public final class TpbCmd extends SimpleCommand {

	/**
	 * Constructeur qui permet d'instancier la super-classe.
	 */
	public TpbCmd() {
		super(PrefixMessage.PREFIX, SenderType.ALL, "Veuillez pr�ciser le joueur !");
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final Player firstPlayer = CommandValidator.getPlayerByString(args[0]);
		CommandValidator.isNotOnline(firstPlayer, "Le joueur '" + args[0] + "' n'est pas en ligne !");
		if (args.length == 1) {
			CommandValidator.isFalse(sender instanceof Player, "Utilisez la commande /tpb <joueur> <joueur> !");
			((Player) sender).teleport(firstPlayer, TeleportCause.PLUGIN);
			super.sendMessage(sender, "Vous vous �tes t�l�port� � " + firstPlayer.getDisplayName() + "�e !");
		} else {
			final Player secondPlayer = CommandValidator.getPlayerByString(args[1]);
			CommandValidator.isNotOnline(secondPlayer, "Le joueur '" + args[1] + "' n'est pas en ligne");
			firstPlayer.teleport(secondPlayer, TeleportCause.PLUGIN);
			super.sendMessage(sender, "Le joueur " + firstPlayer.getDisplayName() + "�e a �t� t�l�port� � "
					+ secondPlayer.getDisplayName() + "�e !");
		}
	}

	@Override
	protected final String getHelp() {
		return "Grace � cette commande vous pourrez vous t�l�porter au joueur que vous voudrez ou encore t�l�porter un joueur � un autre. Cette commande est pr�sente pour les organisateurs.";
	}

	@Override
	protected final List<String> onTabComplete(final String[] args) {
		return (args.length == 1 || args.length == 2) ? PlayerPcm.getCorrectPlayersNames() : Arrays.asList("");
	}

}
