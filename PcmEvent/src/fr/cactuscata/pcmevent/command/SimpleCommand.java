package fr.cactuscata.pcmevent.command;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;

/**
 * Cette classe permet de gerer les commandes "simples", sans argument précis.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see CCommandExecutor
 *
 */

public abstract class SimpleCommand extends CCommandExecutor {

	private final String[] warnMessages;
	private final PrefixMessage prefix;
	private final SenderType[] senderType;

	/**
	 * @param prefix
	 *            Prefix envoyé pour eviter la répétition de code, executution du
	 *            {@link CommandSender#sendMessage(String)} via
	 *            {@link SimpleCommand#sendMessage(CommandSender, String)}.
	 * @param senderType
	 *            Les senders qui peuvent executer la commande.
	 * @param warnMessages
	 *            Messages envoyés si le nombre d'argument ne suiffit pas.
	 */
	protected SimpleCommand(final PrefixMessage prefix, final SenderType[] senderType, final String... warnMessages) {
		this.prefix = prefix;
		this.warnMessages = warnMessages;
		this.senderType = senderType;
	}

	/**
	 * @param prefix
	 *            Prefix envoyé pour eviter la répétition de code, executution du
	 *            {@link CommandSender#sendMessage(String)} via
	 *            {@link SimpleCommand#sendMessage(CommandSender, String)}.
	 * @param senderType
	 *            Le sender qui peut executer la commande.
	 * @param warnMessages
	 *            Messages envoyés si le nombre d'argument ne suiffit pas.
	 */
	protected SimpleCommand(final PrefixMessage prefix, final SenderType senderType, final String... warnMessages) {
		this(prefix, new SenderType[] { senderType }, warnMessages);
	}

	/**
	 * Méthode executé lorsque l'argument de la commande est reconnu.
	 * 
	 * @param sender
	 *            Celui qui a executé la commande.
	 * @param args
	 *            Tableau d'argument représentant les différents arguments.
	 * @throws CommandException
	 *             Si la commande ne respecte pas le paterne voulu.
	 */
	protected abstract void execute(final CommandSender sender, final String[] args) throws CommandException;

	/**
	 * Méthode qui Permet de récuperer l'aide de l'argument.
	 * 
	 * @return La phrase d'aide.
	 */
	protected abstract String getHelp();

	/**
	 * Méthode permettant de vérifier le sender ainsi qu'envoyant l'aide si besoin.
	 * 
	 * @param sender
	 * @param args
	 * @throws CommandException
	 */
	@Override
	protected final void onCommand(final CommandSender sender, final String[] args) throws CommandException {
		CommandValidator.isTrue((args.length == 1) && (args[0].equalsIgnoreCase("help")), this.getHelp());
		CommandValidator.checkSender(sender, this.senderType);
		if (args.length < this.warnMessages.length)
			throw new CommandException(this.warnMessages[args.length]);
		execute(sender, args);
	}

	/**
	 * Méthode pour l'autocompletion qui sera redéfini dans les classes filles à
	 * cette classe.
	 * 
	 * @param args
	 *            Liste des arguments de la commande déjà présents.
	 * 
	 * @return La liste des arguments possibles.
	 */
	@Override
	protected List<String> onTabComplete(final String[] args) {
		return Arrays.asList("");
	}

	/**
	 * Méthode permettant d'envoyer un message avec le préfix précisé dans
	 * {@link SimpleCommand#SimpleCommand(PrefixMessage, SenderType, String...)} et
	 * si le sender est la console, alors les caractères seront formatés de telle
	 * sorte qu'ils seront lisibles grade à la méthode
	 * {@link CCommandExecutor#normalizeMessage(CommandSender, String)}.
	 * 
	 * @param sender
	 *            Sender qui doit recevoir le message.
	 * @param message
	 *            Le message;
	 */
	protected final void sendMessage(final CommandSender sender, final Object message) {
		sender.sendMessage(this.prefix + CCommandExecutor.normalizeMessage(sender, message.toString()));
	}

}
