package fr.cactuscata.pcmevent.command.nosimplecmd.holo;

import java.util.List;

import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SubCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.hologram.HoloFile;
import fr.cactuscata.pcmevent.utils.hologram.objects.NamedHologram;
import fr.cactuscata.pcmevent.utils.hologram.objects.NamedHologramManager;
import fr.cactuscata.pcmevent.utils.other.StringUtils;

/**
 * Cette classe permet l'utilisation de l'argument <code>''</code> de la
 * commande <code>'/holo'</code>.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see {@link HoloCmd}
 */

final class HoloCmdAddLine extends SubCommand {

	private final HoloFile holoFile;

	/**
	 * Définis les valeurs par défaut pour sa super-classe {@link SubCommand}.
	 * 
	 * @param holoFile
	 *            Permet d'ajouter une ligne.
	 */
	HoloCmdAddLine(final HoloFile holoFile) {
		super("addline", SenderType.ALL, "Veuillez préciser le nom de l'hologramme !", "Veuillez préciser le texte !");
		this.holoFile = holoFile;
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final NamedHologram hologram = NamedHologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.isNull(hologram, "L'hologramme '" + args[0] + "' n'existe pas !");
		hologram.getLinesUnsafe().add(holoFile.readLineFromString(StringUtils.join(args, 1, " "), hologram));
		hologram.refreshAll();
		sender.sendMessage(PrefixMessage.PREFIX + "La ligne à  été ajouté avec succès !");
	}

	@Override
	protected final String getHelp() {
		return "Vous pourrez, grace à cet argument ajouter une ligne à l'hologramme précisé.";
	}

	@Override
	protected final List<String> getSubArguments() {
		return NamedHologramManager.getHologramsNames();
	}

}
