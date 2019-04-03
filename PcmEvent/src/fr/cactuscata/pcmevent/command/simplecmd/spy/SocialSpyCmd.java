package fr.cactuscata.pcmevent.command.simplecmd.spy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;

/**
 * <p>
 * Cette classe permet d'executer la commande /socialspy, cette dernière permet
 * de voir les messages privés du {@link MsgCmd /msg} et du {@link RCmd /r}.
 * </p>
 * <p>
 * La commande fonctionne sous forme de 'toggle', c'est à dire qu'une fois
 * activé vous serez mis dans une {@link SocialSpyCmd#social liste} et une fois
 * la commande réactivée, vous serez retiré de la {@link SocialSpyCmd#social
 * liste}.
 * </p>
 * <p>
 * La liste de tous les joueurs exécutant la commande est récupérable via
 * {@link SocialSpyCmd#getSocial()}. Elle est notamment utilisé pour la classe
 * {@link SpyListCmd}.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.0.0
 */

public final class SocialSpyCmd extends SimpleCommand {

	private static final List<CommandSender> social = new ArrayList<CommandSender>();

	/**
	 * Constructeur qui permet d'instancier la super-classe.
	 */
	public SocialSpyCmd() {
		super(PrefixMessage.SPY, new SenderType[] { SenderType.PLAYER, SenderType.CONSOLE });
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final StringBuilder build = new StringBuilder("Vous ");
		if (social.contains(sender)) {
			build.append("ne voyez plus les messages privés !");
			social.remove(sender);
		} else {
			build.append("voyez les messages privés !");
			social.add(sender);
		}
		super.sendMessage(sender, build);
	}

	/**
	 * Méthode qui permet de récuperer la liste de tous les sender qui ont utilisé
	 * la commande <code>'/spy'</code>.
	 * 
	 * @return La liste de tous les {@link CommandSender} qui ont executé la
	 *         commande <code>'/spy'</code>.
	 */
	public static final List<CommandSender> getSocial() {
		return social;
	}

	@Override
	protected final String getHelp() {
		return "Cette commande permet de voir tous les messages privés des joueurs.";
	}

}
