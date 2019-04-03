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
import fr.cactuscata.pcmevent.utils.hologram.objects.lines.CraftHologramLine;
import fr.cactuscata.pcmevent.utils.other.StringUtils;

/**
 * Cette classe permet l'utilisation de l'argument <code>'setline'</code> de la
 * commande <code>'/holo'</code>.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see {@link HoloCmd}
 */

final class HoloCmdSetLine extends SubCommand {

	private final HoloFile holoFile;

	/**
	 * Définis les valeurs par défaut pour sa super-classe {@link SubCommand}.
	 * 
	 * @param holoFile
	 *            Permet de récuperer les méthodes associés à l'objet.
	 */
	HoloCmdSetLine(final HoloFile holoFile) {
		super("setline", SenderType.ALL, "Veuillez préciser le nom de l'hologramme !", "Veuillez préciser la ligne !",
				"Veuillez préciser le texte !");
		this.holoFile = holoFile;
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final NamedHologram hologram = NamedHologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.isNull(hologram, "L'hologramme '" + args[0] + "' n'existe pas !");

		final int lineNumber = CommandValidator.getInt(args[1]);
		CommandValidator.isBetweenTo(lineNumber, 1, hologram.size(),
				"Le nombre de la ligne doit être entre 1 et " + hologram.size() + ".");
		final int index = lineNumber - 1;

		((CraftHologramLine) hologram.getLinesUnsafe().get(index)).despawn();
		hologram.getLinesUnsafe().set(index,
				this.holoFile.readLineFromString(StringUtils.join(args, 2, " "), hologram));
		hologram.refreshAll();

		sender.sendMessage(PrefixMessage.PREFIX + "La ligne " + lineNumber + " a bien été modifié !");
	}

	@Override
	protected final String getHelp() {
		return "Grace à cet argument vous pourrez changer la ligne d'un hologramme.";
	}

	@Override
	protected final List<String> getSubArguments() {
		return NamedHologramManager.getHologramsNames();
	}

}
