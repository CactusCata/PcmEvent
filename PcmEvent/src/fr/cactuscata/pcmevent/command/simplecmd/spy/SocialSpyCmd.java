package fr.cactuscata.pcmevent.command.simplecmd.spy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;

/**
 * <p>
 * Cette classe permet d'executer la commande /socialspy, cette derni�re permet
 * de voir les messages priv�s du {@link MsgCmd /msg} et du {@link RCmd /r}.
 * </p>
 * <p>
 * La commande fonctionne sous forme de 'toggle', c'est � dire qu'une fois
 * activ� vous serez mis dans une {@link SocialSpyCmd#social liste} et une fois
 * la commande r�activ�e, vous serez retir� de la {@link SocialSpyCmd#social
 * liste}.
 * </p>
 * <p>
 * La liste de tous les joueurs ex�cutant la commande est r�cup�rable via
 * {@link SocialSpyCmd#getSocial()}. Elle est notamment utilis� pour la classe
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
			build.append("ne voyez plus les messages priv�s !");
			social.remove(sender);
		} else {
			build.append("voyez les messages priv�s !");
			social.add(sender);
		}
		super.sendMessage(sender, build);
	}

	/**
	 * M�thode qui permet de r�cuperer la liste de tous les sender qui ont utilis�
	 * la commande <code>'/spy'</code>.
	 * 
	 * @return La liste de tous les {@link CommandSender} qui ont execut� la
	 *         commande <code>'/spy'</code>.
	 */
	public static final List<CommandSender> getSocial() {
		return social;
	}

	@Override
	protected final String getHelp() {
		return "Cette commande permet de voir tous les messages priv�s des joueurs.";
	}

}
