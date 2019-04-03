package fr.cactuscata.pcmevent.command;

import java.text.Normalizer;
import java.util.List;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;

/**
 * Classe qui permet de gerer l'execution de commande via l'implémentation de
 * l'interface {@link CommandExecutor} ainsi que la complétion automatique avec
 * la tabultion ({@link TabCompleter}).
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.0.0
 * @see SimpleCommand NotSimpleCommand
 *
 */

public abstract class CCommandExecutor implements TabExecutor {

	/**
	 * Méthode qui permet aux classes filles telles que {@link SimpleCommand} ou
	 * {@link NotSimpleCommand} de gérer les commandes.
	 * 
	 * @param sender
	 *            Sender qui a executé la commande.
	 * @param args
	 *            Tableau des arguments de la commande.
	 * @throws CommandException
	 *             Levé si la commande ne respecte pas le pattern voulu dans les
	 *             classes filles.
	 */
	
	protected abstract void onCommand(final CommandSender sender, final String[] args) throws CommandException;

	/**
	 * Méthode qui permet la complétion automatique avec la tabulation.
	 * 
	 * @param args
	 *            Tableau des arguments de la commande.
	 * @return Selon le nombre d'argument retourne une liste d'arguments possibles.
	 */
	protected abstract List<String> onTabComplete(final String[] args);

	@Override
	public final boolean onCommand(final CommandSender sender, final Command cmd, final String label,
			final String[] args) {
		try {
			this.onCommand(sender, args);
		} catch (final CommandException e) {
			sender.sendMessage(PrefixMessage.ERROR + normalizeMessage(sender, e.getMessage()));
		}
		return true;
	}

	@Override
	public final List<String> onTabComplete(final CommandSender sender, final Command cmd, final String label,
			final String[] args) {
		return this.onTabComplete(args);
	}

	/**
	 * Méthode qui permet, si le sender instancie l'interface
	 * {@link ConsoleCommandSender} de simplifier le message de telle sorte à
	 * retirer les accents ainsi que les caractères non-UTF-8.
	 * 
	 * @param sender
	 *            Sender qui a executé la commande.
	 * @param message
	 *            Message initiale cible.
	 * @return Le message normalisé.
	 */
	static final String normalizeMessage(final CommandSender sender, final String message) {
		return sender instanceof ConsoleCommandSender
				? Normalizer.normalize(message, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
				: message;
	}

	/**
	 * Enumeration qui permet de séléctionner le/les sender voulu pour l'execution
	 * de certaines commandes ou arguments.
	 * 
	 * @author CactusCata
	 * @version 2.5.1
	 * @since 2.5.0
	 */
	public static enum SenderType {
		ALL(null),
		CONSOLE("la console"),
		PLAYER("un joueur"),
		COMMAND_BLOCK("un bloc de commande");

		private final String senderName;

		/**
		 * @param senderName
		 *            Nom d'affichage.
		 */
		private SenderType(final String senderName) {
			this.senderName = senderName;
		}

		/**
		 * Méthode qui retourne le nom d'affichage de chaque sender.
		 * 
		 * @return La nom d'affichage du sender.
		 */
		final String getSenderName() {
			return this.senderName;
		}

		/**
		 * Méthode vérifie si un {@link CommandSender} est bien égal à un
		 * {@link SenderType}.
		 * 
		 * @param sender
		 *            Sender cible.
		 * @param senderType
		 *            {@link SenderType} cible.
		 * @return Vrai si le sender correspond à sa valeur en tant que SenderType.
		 */
		public static final boolean checkSenderTypeIsCorrect(final CommandSender sender, final SenderType senderType) {
			return !(senderType != SenderType.ALL && senderType == SenderType.PLAYER && !(sender instanceof Player)
					|| senderType == SenderType.CONSOLE && !(sender instanceof ConsoleCommandSender)
					|| senderType == SenderType.COMMAND_BLOCK && !(sender instanceof BlockCommandSender));

		}

	}
	
}
