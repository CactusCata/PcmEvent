package fr.cactuscata.pcmevent.command.nosimplecmd.physic;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SubCommand;

/**
 * Cette classe permet le fonctionnement de l'argument <code>'remoev'</code> de
 * la commande <code>'/physic'</code>.
 * 
 * @author Cactuscata
 * @version 2.5.1
 * @since 2.5.1
 * @see {@link PhysicCmd}.
 */

final class PhysicCmdRemove extends SubCommand {

	private final PhysicFile physicFile;

	/**
	 * Définis les valeurs par défaut pour sa super-classe {@link SubCommand}.
	 * 
	 * @param physicFile
	 *            Permet de retirer le material voulu via
	 *            {@link PhysicFile#getList()}.
	 */
	PhysicCmdRemove(final PhysicFile physicFile) {
		super("remove", SenderType.ALL, "Veuillez préciser le material !");
		this.physicFile = physicFile;
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		args[0] = args[0].toUpperCase();
		@SuppressWarnings("deprecation")
		final Material material = CommandValidator.isInteger(args[0]) ? Material.getMaterial(Integer.parseInt(args[0]))
				: Material.getMaterial(args[0]);
		CommandValidator.isNull(material, "La material '" + args[0] + "' n'existe pas !");
		CommandValidator.isTrue(!this.physicFile.getList().contains(material),
				"La liste ne contient pas le material '" + material.toString() + "' !");
		this.physicFile.getList().remove(material);
		sender.sendMessage(
				PrefixMessage.PREFIX + "le material '" + material.toString() + " a été retiré avec succès !");
	}

	@Override
	protected final String getHelp() {
		return "Vous pouvez grace à cet argument, retirer un material qui est présent dans la liste (/physic list)";
	}

}
