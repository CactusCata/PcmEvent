package fr.cactuscata.pcmevent.command;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.utils.other.StringUtils;

/**
 * Cette classe permet de donner la liberté d'avoir une classe par premier
 * premier argument. Elle dispose aussi d'un aide
 * ({@link NotSimpleCommand#getTutorialCommand()}) et peut aussi fonctionner
 * sans mais doit implementer l'interface {@link ExecutableCommand}.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see CCommandExecutor SubCommand ExecutableCommand
 *
 */

public abstract class NotSimpleCommand extends CCommandExecutor {

	private final SubCommand[] subCommands;
	private final String argumentsNames;

	/**
	 * 
	 * @param arguments
	 *            Tous les premiers arguments de la commande spécifié.
	 */
	protected NotSimpleCommand(SubCommand... arguments) {
		this.subCommands = arguments;
		this.argumentsNames = StringUtils.join(StringUtils.toStringArray(this.subCommands), ", ");
	}

	/**
	 * Méthode permettant d'avoir l'aide de la commande.
	 * 
	 * @return La phrase d'aide.
	 */
	protected abstract String getTutorialCommand();

	/**
	 * Méthode éxecuté lorsque qu'un sender utilise une commande.
	 * 
	 * @param sender
	 *            Sender qui a executé la commande.
	 * @param args
	 *            Arguments de la commande.
	 * @throws CommandException
	 *             Si la commande ne respecte pas le perterne de la commande ou des
	 *             sous arguments.
	 */
	@Override
	protected final void onCommand(final CommandSender sender, final String[] args) throws CommandException {

		if (args.length == 0) {
			CommandValidator.isFalse(this instanceof ExecutableCommand,
					this.getTutorialCommand() + (this.subCommands.length == 0 ? ""
							: "Vous pouvez utiliser " + (this.subCommands.length == 1 ? "l'" : "les") + " argument"
									+ (this.subCommands.length == 1 ? "" : "s") + ": " + this.argumentsNames + "."));
			((ExecutableCommand) this).execute(sender, args);
			return;
		}

		CommandValidator.isTrue(args[0].equalsIgnoreCase("help"), this.getTutorialCommand());

		for (final SubCommand subCommand : this.subCommands) {
			if (args[0].equalsIgnoreCase(subCommand.toString())) {
				if (subCommand instanceof ExecutableCommand)
					((ExecutableCommand) subCommand).execute(sender, StringUtils.removeIndex(0, args));
				else
					subCommand.onCommand(sender, StringUtils.removeIndex(0, args));
				return;

			}

		}

		throw new CommandException("L'argument '" + args[0] + "' est inconnu, essayez: " + this.argumentsNames);

	}

	/**
	 * Méthode qui permet la tabulation.
	 * 
	 * @param args
	 *            Liste des arguments.
	 * @return La liste des arguments dans la tabulation.
	 */
	@Override
	protected final List<String> onTabComplete(final String[] args) {
		if (args.length == 1)
			return Arrays.asList(StringUtils.toStringArray(this.subCommands));
		else if (args.length == 2) {
			for (final SubCommand subCommand : this.subCommands)
				if (args[0].equalsIgnoreCase(subCommand.toString()))
					return subCommand.getSubArguments();
		}
		return Arrays.asList("");
	}

}
