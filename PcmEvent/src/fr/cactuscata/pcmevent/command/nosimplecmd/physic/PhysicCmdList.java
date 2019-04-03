package fr.cactuscata.pcmevent.command.nosimplecmd.physic;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SubCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;

/**
 * Cette classe permet le fonctionnement de l'argument <code>'list'</code> de la
 * commande <code>'/physic'</code>.
 * 
 * @author Cactuscata
 * @version 2.5.1
 * @since 2.5.1
 * @see {@link PhysicCmd}.
 */

final class PhysicCmdList extends SubCommand {

	private final PhysicFile physicFile;

	/**
	 * Définis les valeurs par défaut pour sa super-classe {@link SubCommand}.
	 * 
	 * @param physicFile
	 *            Permet d'afficher la liste de tous les materials whitelist
	 *            {@link PhysicFile#getList()}.
	 */
	PhysicCmdList(final PhysicFile physicFile) {
		super("list", SenderType.ALL);
		this.physicFile = physicFile;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {

		final int page = args.length > 0 ? CommandValidator.getInt(args[0]) : 1;

		CommandValidator.isTrue(page < 1, "La page précisé doit être entre 1 ou suprieur !");

		int totalPages = this.physicFile.getList().size() / 10;
		if (this.physicFile.getList().size() % 10 != 0)
			totalPages++;

		CommandValidator.isTrue(this.physicFile.getList().isEmpty(), "Aucun material n'a été ajouté pour l'instant !");

		sender.sendMessage(PrefixMessage.PREFIX + "Liste des materials (Page " + page + " / " + totalPages + ")");
		final int fromIndex = (page - 1) * 10;

		for (int i = fromIndex, toIndex = fromIndex + 10; i < toIndex; i++) {
			if (i < this.physicFile.getList().size()) {
				final Material material = this.physicFile.getList().get(i);
				sender.sendMessage("§e- " + material.toString() + " (ID: " + material.getId() + ")");
			}
		}
		if (page < totalPages)
			sender.sendMessage(
					PrefixMessage.PREFIX + "Regardez la page suivante avec la commande /physic list " + (page + 1));

	}

	@Override
	protected final String getHelp() {
		return "Vous pouvez grace à cet argument récuperer la liste de tous les materials ";
	}

}
