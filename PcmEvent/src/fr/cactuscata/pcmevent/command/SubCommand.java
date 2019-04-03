package fr.cactuscata.pcmevent.command;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;

/**
 * Cette classe permet de supporter et gerer chaques arguments indépendamment.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see NotSimpleCommand CCommandExecutor
 */

public abstract class SubCommand {

	private final SenderType[] senderTypes;
	private final String subCommandName;
	private final String[] messagesArgs;

	/**
	 * @param subCommandName
	 *            L'argument sous forme de {@link String} qui sera ensuite reconnu
	 *            dans {@link SubCommand#onCommand(CommandSender, String[])}.
	 * @param senderType
	 *            Le seul sender possible.
	 * @param messageArgs
	 *            Messages envoyés si il n'y a pas assez d'arguments.
	 */
	protected SubCommand(final String subCommandName, final SenderType[] senderType, final String... messagesArgs) {
		this.senderTypes = senderType;
		this.subCommandName = subCommandName;
		this.messagesArgs = messagesArgs;
	}

	/**
	 * @param subCommandName
	 *            L'argument sous forme de {@link String} qui sera ensuite reconnu
	 *            dans {@link SubCommand#onCommand(CommandSender, String[])}.
	 * @param senderType
	 *            Le seul sender possible.
	 * @param messageArgs
	 *            Messages envoyés si il n'y a pas assez d'arguments.
	 */
	protected SubCommand(final String subCommandName, final SenderType senderType, final String... messageArgs) {
		this(subCommandName, new SenderType[] { senderType }, messageArgs);
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
	protected final void onCommand(final CommandSender sender, final String[] args) throws CommandException {

		CommandValidator.checkSender(sender, this.senderTypes);
		if (args.length < this.messagesArgs.length)
			throw new CommandException(this.messagesArgs[args.length]);
		CommandValidator.isTrue((args.length == 1) && (args[0].equalsIgnoreCase("help")), this.getHelp());
		this.execute(sender, args);

	}

	/**
	 * Méthode qui permet de récupere une liste qui serviva d'aide tabulation.
	 * 
	 * @return La liste de toutes les entrées dans l'object {@link VanishFile}.
	 */
	protected List<String> getSubArguments() {
		return Arrays.asList("");
	}

	@Override
	public final String toString() {
		return this.subCommandName;
	}

}
