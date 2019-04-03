package fr.cactuscata.pcmevent.command.simplecmd;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.other.StringUtils;

/**
 * <p>
 * Classe qui permet de d'executer la commande /broadcast et qui permet avec
 * cela d'envoyer un message dans le chat pour tout les joueurs
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.0.0
 */
public final class BroadcastCmd extends SimpleCommand {

	/**
	 * Constructeur qui permet d'instancier la super-class.
	 */
	public BroadcastCmd() {
		super(PrefixMessage.PREFIX, SenderType.ALL, "Veuillez préciser le message");
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final String message = StringUtils.join(args, " ").replace('&', '§');
		Bukkit.getOnlinePlayers().forEach(playerOnline -> playerOnline.sendMessage(message));
	}

	@Override
	protected final String getHelp() {
		return "Cette commande permet de rédiger une annonce qui sera envoyé à tous les joueurs du monde event.";
	}

}
