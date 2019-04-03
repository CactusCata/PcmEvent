package fr.cactuscata.pcmevent.command.simplecmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;

/**
 * <p>
 * Cette classe permet d'executer la commande /fly, prévus pour les
 * organisateurs, cette commande leur permet d'obtenir la possibilité de vol.
 * </p>
 * <p>
 * La commande fonctionne sous forme de 'toggle', son execution changera votre
 * possiilité de voler.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.0.0
 */

public final class FlyCmd extends SimpleCommand {

	/**
	 * Constructeur qui permet d'instancier la super-class.
	 */
	public FlyCmd() {
		super(PrefixMessage.PREFIX, SenderType.PLAYER);
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final Player playerSender = (Player) sender;

		playerSender.setAllowFlight(!playerSender.getAllowFlight());
		super.sendMessage(sender,
				"Vous " + (playerSender.getAllowFlight() ? "pouvez désormais voler" : "ne volez plus") + " !");

	}

	@Override
	protected final String getHelp() {
		return "Cette commande permet de voler à la manière du gamemode créatif. Cette commande est principalement faite pour les Orgnisateurs.";
	}

}
