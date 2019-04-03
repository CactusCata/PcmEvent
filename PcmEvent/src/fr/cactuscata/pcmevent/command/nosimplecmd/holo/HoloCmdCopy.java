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

/**
 * Cette classe permet l'utilisation de l'argument <code>'copy'</code> de la
 * commande <code>'/holo'</code>.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see {@link HoloCmd}
 */

final class HoloCmdCopy extends SubCommand {

	private final HoloFile holoFile;

	/**
	 * Définis les valeurs par défaut pour sa super-classe {@link SubCommand}.
	 * 
	 * @param holoFile
	 *            permet de copié un hologromme.
	 */
	HoloCmdCopy(final HoloFile holoFile) {
		super("copy", SenderType.ALL, "Veuillez préciser le nom de l'hologramme !",
				"Veuillez préciser le nom de l'hologramme !");
		this.holoFile = holoFile;
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final NamedHologram hologramToCopy = NamedHologramManager.getHologram(args[0].toLowerCase()),
				intoHologram = NamedHologramManager.getHologram(args[1].toLowerCase());
		CommandValidator.isNull(hologramToCopy, "L'hologramme '" + args[0] + "' n'existe pas !");
		CommandValidator.isNull(intoHologram, "L'hologramme '" + args[1] + "' n'existe pas !");

		intoHologram.clearLines();
		hologramToCopy.getLinesUnsafe().forEach(craftHoloLine -> intoHologram.getLinesUnsafe()
				.add(this.holoFile.readLineFromString(this.holoFile.saveLineToString(craftHoloLine), intoHologram)));
		intoHologram.refreshAll();

		sender.sendMessage(PrefixMessage.PREFIX + "L'hologramme \"" + hologramToCopy.getName()
				+ "\" à bien été copié depuis \"" + intoHologram.getName() + "\"!");
	}

	@Override
	protected final String getHelp() {
		return "Cet argument permet de copier les données d'un hologramme à un autre";
	}

	@Override
	protected final List<String> getSubArguments() {
		return NamedHologramManager.getHologramsNames();
	}

}
