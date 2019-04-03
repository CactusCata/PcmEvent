package fr.cactuscata.pcmevent.command.simplecmd.spy;

import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.other.StringUtils;

/**
 * <p>
 * Cette classe permet d'executer la commande /spylist, cette dernière est
 * utilisé pour récuperer la liste de toutes les personnes qui ont utilisé la
 * commande {@link SocialSpyCmd /socialspy}.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.0.0
 */

public final class SpyListCmd extends SimpleCommand {

	/**
	 * Constructeur qui permet d'instancier la super-classe.
	 */
	public SpyListCmd() {
		super(PrefixMessage.SPY, SenderType.ALL);
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		CommandValidator.listIsEmpty(SocialSpyCmd.getSocial(), "Personne n'a le spy d'activé !");
		super.sendMessage(sender, "Liste des gens qui ont le spy d'activé: " + StringUtils.join(
				SocialSpyCmd.getSocial().stream().map(senders -> senders.getName()).collect(Collectors.toList()),
				", "));
	}

	@Override
	protected final String getHelp() {
		return "Cette commande permet de connaitre tous les joueurs qui ont le social spy d'activé.";
	}

}
