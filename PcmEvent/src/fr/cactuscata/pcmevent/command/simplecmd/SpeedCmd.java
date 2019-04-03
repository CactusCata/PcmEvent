package fr.cactuscata.pcmevent.command.simplecmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;

/**
 * <p>
 * Cette classe permet d'executer la commande /speed, cette dernière permet
 * d'aller plus vite, que ça sois dans les airs ou sur la terre.
 * </p>
 * <p>
 * La commande doit avoir pour argument un nombre entre un et dix.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.0.0
 */

public final class SpeedCmd extends SimpleCommand {

	/**
	 * Constructeur qui permet d'instancier la super-class.
	 */
	public SpeedCmd() {
		super(PrefixMessage.PREFIX, SenderType.PLAYER, "Veuillez préciser la puissance du speed !");
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final float amount = CommandValidator.getFloat(args[0]);
		CommandValidator.isBetweenTo(amount, 0, 100);
		final Player playerSender = (Player) sender;
		playerSender.setWalkSpeed(amount / 10);
		playerSender.setFlySpeed(amount / 20);
		super.sendMessage(sender, String.format("Votre vitesse à été mise à %f ! ( 2 par défaut )", amount));

	}

	@Override
	protected final String getHelp() {
		return "Grace à cette commande vous pourrez modifier votre vitesse déplacement ainsi que celle de vol.";
	}

}
