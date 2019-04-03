package fr.cactuscata.pcmevent.command;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.utils.bukkit.UUIDFetcher;
import fr.cactuscata.pcmevent.utils.other.StringUtils;

/**
 * Cette classe permet de vérifier des vérités et si elles sont pas respectés,
 * l'exception {@link CommandException} est levé (pour la plus part des cas).
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see CCommandExecutor
 *
 */

public final class CommandValidator {

	/**
	 * Méthode qui permet la conversion d'une chaîne de caratère en nombre entier.
	 * 
	 * @param stringInt
	 *            La chaine de caractères qui doit être convertie en nombre entier.
	 * @return Le nombre voulu.
	 * @throws CommandException
	 *             Si la chaîne de caractère n'est pas un nombre.
	 */
	public static final int getInt(final String stringInt) throws CommandException {
		try {
			return Integer.parseInt(stringInt);
		} catch (final NumberFormatException e) {
			throw new CommandException("Le nombre '" + stringInt + "' n'est pas un nombre entier valide !");
		}
	}

	/**
	 * Méthode qui permet la conversion d'une chaîne de caractère en nombre à
	 * virgule.
	 * 
	 * @param stringFloat
	 *            La chaîne de caractère qui doit être convertie en nombre à
	 *            virgule.
	 * @return Le nombre à virgule.
	 * @throws CommandException
	 *             Si la chaîne de caractère n'est pas convertissable en nombre à
	 *             virgule.
	 */
	public static final float getFloat(final String stringFloat) throws CommandException {
		try {
			return Float.parseFloat(stringFloat);
		} catch (final NumberFormatException e) {
			throw new CommandException("Le nombre '" + stringFloat + "' n'est pas un nombre à valide !");
		}
	}

	/**
	 * Méthode qui lève l'exception {@link CommandException} si le boolean choisi
	 * est vrai.
	 * 
	 * @param booleanValue
	 *            Le boolean en question.
	 * @param message
	 *            Message envoyé si le boolean est vrai.
	 * @throws CommandException
	 *             Si le boolean est vrai.
	 */
	public static final void isTrue(final boolean booleanValue, final String message) throws CommandException {
		if (booleanValue)
			throw new CommandException(message);
	}

	/**
	 * Méthode qui lève l'exception {@link CommandException} si le boolean choisi
	 * est vrai.
	 * 
	 * @param booleanValue
	 *            Le boolean en question.
	 * @param message
	 *            Message envoyé si le boolean est vrai.
	 * @throws CommandException
	 *             Si le boolean est vrai.
	 */
	public static final void isFalse(final boolean booleanValue, final String message) throws CommandException {
		isTrue(!booleanValue, message);
	}

	/**
	 * Méthode qui vérifie le nombre d'argument présent et lève l'exception
	 * {@link CommandException} si le nombre d'argument n'est pas assez grand.
	 * 
	 * @param args
	 *            Le tableau cible.
	 * @param index
	 *            Index.
	 * @param message
	 *            Message envoyé si le tableau cibleau à une taile inférieur à
	 *            l'index.
	 * @throws CommandException
	 *             Exception levé si tableau cibleau à une taile inférieur à
	 *             l'index.
	 */
	public static final void argsSizeIsPresent(final String[] args, final int index, final String message)
			throws CommandException {
		if (args.length < index)
			throw new CommandException(message);
	}

	/**
	 * Méthode qui vérifie le nombre d'arguments présents et envois un message si
	 * besoin.
	 * 
	 * @param args
	 *            Tableau cible.
	 * @param messages
	 *            Tableau de message correctif.
	 * @throws CommandException
	 *             Exception levé si
	 *             <code> (args.length &lsaquo; messages.lentgh) </code>.
	 */
	public static final void argsSizeIsPresent(final String[] args, final String... messages) throws CommandException {
		if (args.length < messages.length)
			throw new CommandException(messages[args.length]);
	}

	/**
	 * Méthode qui récupere un joueur connecté par son pseudo.
	 * 
	 * @param playerName
	 *            Pseudo du joueur.
	 * @return Le joueur (peut être null si le joueur spécifié n'est pas connecté).
	 */
	public static final Player getPlayerByString(final String playerName) {
		return Bukkit.getPlayerExact(playerName);
	}

	/**
	 * Méthode qui envois une exception si l'objet spécifié est null.
	 * 
	 * @param object
	 *            Objet cible.
	 * @param message
	 *            Message envoyé si l'objet est égal à null.
	 * @throws CommandException
	 *             Exception levé si l'objet est égal à null.
	 */
	public static final void isNull(final Object object, final String message) throws CommandException {
		if (object == null)
			throw new CommandException(message);
	}

	/**
	 * Méthode qui renvois un un boolean qui déterminera si le joueur spécifié est
	 * bien connecté via
	 * <code>(player != null &amp; &amp; player.isOnline())</code>.
	 * 
	 * @param player
	 *            Le joueur cible.
	 * @return Vrai si le joueur est connecté.
	 */
	public static final boolean isOnline(final Player player) {
		return player != null && player.isOnline();
	}

	/**
	 * Méthode qui lève l'exception {@link CommandException} si le joueur n'est pas
	 * en ligne.
	 * 
	 * @param player
	 *            Le joueur cible.
	 * @param message
	 *            Le message de l'exception.
	 * @throws CommandException
	 *             Se lève si le joueur n'est en ligne.
	 */
	public static final void isNotOnline(final Player player, final String message) throws CommandException {
		if (!isOnline(player))
			throw new CommandException(message);
	}

	/**
	 * Méthode qui lève l'exception {@link CommandException} si le nombre spécifé
	 * est supérieur à une autre valeur.
	 * 
	 * @param amount
	 *            Le nombre cible.
	 * @param secondAmount
	 *            La valeur fixe.
	 * @throws CommandException
	 *             Se lève si la première valeur est supérieur à la seconde.
	 */
	public static final void isSuperiorTo(final Number amount, final Number secondAmount) throws CommandException {
		isSuperiorTo(amount, secondAmount, "Le nombre " + amount + " doit être supérieur à " + secondAmount + " !");
	}

	/**
	 * Méthode qui lève l'exception {@link CommandException} si le nombre spécifé
	 * est supérieur à une autre valeur.
	 * 
	 * @param amount
	 *            Le nombre cible.
	 * @param secondAmount
	 *            La valeur fixe.
	 * @param message
	 *            Le message d'erreur envoyé.
	 * @throws CommandException
	 *             Se lève si la première valeur est supérieur à la seconde.
	 */
	public static final void isSuperiorTo(final Number amount, final Number secondAmount, final String message)
			throws CommandException {
		if (amount.floatValue() > secondAmount.floatValue())
			throw new CommandException("Le nombre " + amount + " doit être supérieur à " + secondAmount + " !");
	}

	/**
	 * Méthode qui lève l'exception {@link CommandException} si le premier est
	 * inférieur au second.
	 * 
	 * @param amount
	 *            Le nombre cible.
	 * @param secondAmount
	 *            La valeur fixe.
	 * @throws CommandException
	 *             Se lève si la premiere valeur est inférieur à la seconde.
	 */
	public static final void isInferiorTo(final Number amount, final Number secondAmount) throws CommandException {
		isInferiorTo(amount, secondAmount, "Le nombre " + amount + " doit être inférieur à " + secondAmount + " !");
	}

	/**
	 * Méthode qui lève l'exception {@link CommandException} si le premier est
	 * inférieur au second.
	 * 
	 * @param amount
	 *            Le nombre cible.
	 * @param secondAmount
	 *            La valeur fixe.
	 * @param message
	 *            Le message d'erreur à envoyer.
	 * @throws CommandException
	 *             Se lève si la premiere valeur est inférieur à la seconde.
	 */
	public static final void isInferiorTo(final Number amount, final Number secondAmount, final String message)
			throws CommandException {
		if (amount.floatValue() < secondAmount.floatValue())
			throw new CommandException(message);
	}

	public static final void isBetweenTo(final Number value, final Number min, final Number max)
			throws CommandException {
		isBetweenTo(value, min, max, String.format("Le nombre '%f' doit être inférieur à %f et supérieur à %f",
				value.floatValue(), max.floatValue(), min.floatValue()));
	}

	public static final void isBetweenTo(final Number value, final Number min, final Number max, final String message)
			throws CommandException {
		if (value.floatValue() < min.floatValue() || value.floatValue() > max.floatValue())
			throw new CommandException(message);
	}

	/**
	 * Méthode qui récupere l'{@link UUID} du joueur précisé sous forme de
	 * {@link String} grace à la méthode {@link UUIDFetcher#getUUIDOf(String)}. Si
	 * l'UUID n'existe pas, alors l'exception {@link CommandException} est levé.
	 * 
	 * @param playerName
	 *            Le nom du joueur.
	 * @return L' {@link UUID} du joueeur cible.
	 * @throws CommandException
	 *             Si le joueur n'existe pas.
	 */
	public static final UUID getUUIDByName(final String playerName) throws CommandException {
		final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
		if (uuid == null)
			throw new CommandException("Le joueur '" + playerName + "' n'existe pas !");
		return uuid;
	}

	/**
	 * Méthode qui permet de verifié l'égalité d'un objet à un autre via
	 * {@link Object#equals(Object)} et lève l'exception {@link CommandException} si
	 * la valeur de retour est égale à <code>true</code>.
	 * 
	 * @param first
	 *            Premier objet cible.
	 * @param second
	 *            Second objet cible.
	 * @param message
	 *            Message envoyé dans l'exception si la valeur de retour est égale à
	 *            <code>true</code>.
	 * @throws CommandException
	 *             Levé si la valeur de retour est égale <code>true</code>.
	 */
	public static final void is(final Object first, final Object second, final String message) throws CommandException {
		if (first.equals(second))
			throw new CommandException(message);
	}

	/**
	 * Méthode qui verifie la non-égalité entre deux objets via
	 * !{@link Object#equals(Object)} et lève l'exception {@link CommandException}
	 * si la les deux objets ne sont pas égaux.
	 * 
	 * @param first
	 *            Premier objet cible.
	 * @param second
	 *            Second objet cible.
	 * @param message
	 *            Message si l'exception est levé.
	 * @throws CommandException
	 *             Levé si le premier objet n'est pas égal au second.
	 */
	public static final void isNot(final Object first, final Object second, final String message)
			throws CommandException {
		if (!first.equals(second))
			throw new CommandException(message);
	}

	/**
	 * Méthode qui vérifie si une liste est vide, si <code>true</code> est retourné
	 * alors l'exception {@link CommandException} est levé.
	 * 
	 * @param list
	 *            Liste cible.
	 * @param message
	 *            Message envoyé si l'excetption est levé.
	 * @throws CommandException
	 *             Exception levé si la liste ciblé est vide.
	 */
	public static final void listIsEmpty(final List<?> list, final String message) throws CommandException {
		isTrue(list.isEmpty(), message);
	}

	/**
	 * Méthode qui permet de vérifier quelle est l'interface qui hérite de
	 * {@link CommandSender} pour lever l'exception {@link CommandException} si
	 * aucun sender n'a été trouvé.
	 * 
	 * @param sender
	 *            Sender cible.
	 * @param senderTypes
	 *            Tableau de sender accessibles.
	 * @throws CommandException
	 *             Levé si aucun sender approprié n'a été trouvé.
	 */
	public static final void checkSender(final CommandSender sender, final SenderType... senderTypes)
			throws CommandException {
		for (final SenderType senderT : senderTypes) {
			if (SenderType.checkSenderTypeIsCorrect(sender, senderT)) {
				return;
			}
		}

		if (senderTypes.length == 1)
			throw new CommandException(
					"Il n'y a que " + senderTypes[0].getSenderName() + " qui peut executer la commande !");
		else {
			final String[] args = new String[senderTypes.length];
			for (int i = 0, j = args.length; i < j; i++)
				args[i] = senderTypes[i].getSenderName();
			throw new CommandException(
					"Les seuls sender qui peuvent envoyer la commande sont " + StringUtils.join(args, ", ") + " !");
		}

	}

	/**
	 * Méthode qui vérifie si le {@link String} en argument est un nombre entier
	 * 
	 * @param intValue
	 *            Le {@link String} a converitr en nombre si possible
	 * @return <code>True</code> si le nombre est un nombre entier,
	 *         <code>flase</code> sinon.
	 */
	public static final boolean isInteger(final String intValue) {
		try {
			Integer.parseInt(intValue);
			return true;
		} catch (final NumberFormatException e) {
			return false;
		}
	}
}
