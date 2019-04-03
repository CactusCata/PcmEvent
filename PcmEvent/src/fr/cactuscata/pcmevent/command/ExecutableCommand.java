package fr.cactuscata.pcmevent.command;

import org.bukkit.command.CommandSender;

/**
 * Classe qui permet de faire hériter de la méthode
 * {@link ExecutableCommand#execute(CommandSender, String[])} toutes les classes
 * qui hérites de la classe {@link NotSimpleCommand}. Cette classe est
 * uniquement constitué pour cela.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see CCommandExecutor NotSimpleCommand
 *
 */

public interface ExecutableCommand {

	/**
	 * Méthode qui permet à des classes qui hérites de {@link NotSimpleCommand}
	 * d'être executables.
	 * 
	 * @param sender
	 *            Sender qui a executé la commande.
	 * @param args
	 *            Liste des arguments.
	 * @throws CommandException
	 *             Exception levé si la commande ne respecte pas la structure voulu.
	 */
	public void execute(final CommandSender sender, final String[] args) throws CommandException;

}
