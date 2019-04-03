package fr.cactuscata.pcmevent.command.simplecmd;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PlayerPcm;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;

/**
 * <p>
 * Cette classe permet d'executer la commande /heal, cette dernière va redonner
 * toute la vie et la saturation au joueur qui a executé cette commande sans
 * avoir spécifié de joueur ({@link HealCmd#healh(Player)}).
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.0.0
 */

public final class HealCmd extends SimpleCommand {

	/**
	 * Constructeur qui permet d'instancier la super-class.
	 */
	public HealCmd() {
		super(PrefixMessage.PREFIX, SenderType.ALL);
	}

	@Override
	public final void execute(final CommandSender sender, final String[] args) throws CommandException {

		if (args.length == 0)
			if (!(sender instanceof Player))
				sender.sendMessage(PrefixMessage.SENDER_BE_PLAYER.toString());
			else
				healh((Player) sender);
		else {
			final Player player = CommandValidator.getPlayerByString(args[0]);
			CommandValidator.isNotOnline(player, "Le joueur '" + args[0] + "' n'est pas en ligne !");
			healh(player);
			super.sendMessage(sender, "Vous avez heal le joueur " + args[0] + " !");
		}

	}

	/**
	 * Méthode qui permet de redonner toute la vie au joueur ainsi qu'un peu de
	 * saturation.
	 * 
	 * @param player
	 *            Joueur healé.
	 */
	private final void healh(final Player player) {
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.setSaturation(5.0f);
		super.sendMessage(player, "Vous avez été heal !");
	}

	@Override
	protected final String getHelp() {
		return "Grace à cette commande vous pourrez régénerer toute la vie du joueur précisé, par défaut, c'est vous.";
	}

	@Override
	protected final List<String> onTabComplete(final String[] args) {
		return (args.length == 1) ? PlayerPcm.getCorrectPlayersNames() : Arrays.asList("");
	}

}
