package fr.cactuscata.pcmevent.command.nosimplecmd.config;

import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.command.simplecmd.vanish.VanishFile;
import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.SubCommand;

/**
 * Cette classe permet de récuperer la liste des clefs et leurs valeurs dans
 * l'objet {@link VanishFile} avec la méthode {@link VanishFile#getAllList()}.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.5.0
 * @see {@link VanishFile}, {@link ConfigCmd}, {@link ConfigCmdChange}.
 *
 */

final class ConfigCmdList extends SubCommand {

	private final VanishFile vanishFile;

	/**
	 * Garde en mémoire l'objet {@link VanishFile}.
	 * 
	 * @param vanishFile
	 *            Stocké pour afficher plus tard la liste de toutes les clefs de
	 *            la configuration ainsi que ses valeurs dans la méthode
	 *            {@link ConfigCmdList#execute(CommandSender, String[])}.
	 */
	ConfigCmdList(final VanishFile vanishFile) {
		super("list", SenderType.ALL);
		this.vanishFile = vanishFile;
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		sender.sendMessage(this.vanishFile.getAllList());
	}

	@Override
	protected final String getHelp() {
		return "Cet argument permet récuperer la liste de tous les paths de la configuration.";
	}

}
