package fr.cactuscata.pcmevent.command.simplecmd.tp;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
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
 * Cette classe permet l'éxecution de la commande /tppos, cette dernière permet
 * la téléportation du joueur à des coordonées. Elle est utilisé principalement
 * pour les mini jeux en command block ou pour les organisateurs.
 * </p>
 * 
 * @author CactusCata
 * @version 2.4.1
 * @since 1.0.0
 */

public final class TpposCmd extends SimpleCommand {

	/**
	 * Constructeur qui permet d'instancier la super-classe.
	 */
	public TpposCmd() {
		super(PrefixMessage.PREFIX, SenderType.ALL, "Veuillez préciser le joueur !", "Veuillez préciser le 'x' ! ",
				"Veuillez préciser le 'y' !", "Veuillez préciser le 'z' !");
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final Player player = CommandValidator.getPlayerByString(args[0]);
		CommandValidator.isNotOnline(player, "Le joueur '" + args[0] + "' n'est pas connecté !");
		player.teleport(new Location(player.getWorld(), CommandValidator.getFloat(args[1]),
				CommandValidator.getFloat(args[2]), CommandValidator.getFloat(args[3])), TeleportCause.PLUGIN);
		super.sendMessage(sender, "Le joueur '" + args[0] + "' a bien été téléporté !");
		super.sendMessage(player, "Vous avez été  téléporté !");
	}

	@Override
	protected final String getHelp() {
		return "Grace à cette commande, le joueur précisé sera téléporté aux coordonées précisés.";
	}

	@Override
	protected final List<String> onTabComplete(final String[] args) {
		return (args.length == 1) ? PlayerPcm.getCorrectPlayersNames() : Arrays.asList("");
	}

}
