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
 * Cette classe permet l'utilisation de l'argument <code>'insertline'</code> de
 * la commande <code>'/holo'</code>.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see {@link HoloCmd}
 */

final class HoloCmdInsertLine extends SubCommand {

	private final HoloFile holoFile;

	/**
	 * Définis les valeurs par défaut pour sa super-classe {@link SubCommand}.
	 * 
	 * @param holoFile
	 *            Permet de modifier l'hologramme.
	 */
	HoloCmdInsertLine(final HoloFile holoFile) {
		super("insertline", SenderType.ALL, "Veuillez préciser le nom de l'hologramme !",
				"Veuillez préciser la ligne !", "Veuillez préciser le texte !");
		this.holoFile = holoFile;
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final NamedHologram hologram = NamedHologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.isNull(hologram, "L'hologramme '" + args[0] + "' n'existe pas !");

		final int insertAfter = CommandValidator.getInt(args[1]), oldLinesAmount = hologram.size();
		CommandValidator.isBetweenTo(insertAfter, 0, oldLinesAmount);

		hologram.getLinesUnsafe().add(insertAfter,
				this.holoFile.readLineFromString(StringUtils.join(args, 2, " "), hologram));
		hologram.refreshAll();

		sender.sendMessage(PrefixMessage.PREFIX + (insertAfter == 0 ? "La ligne a été inseré avant la ligne 1 !"
				: insertAfter == oldLinesAmount
						? "La ligne a été ajouté à la fin, \nUtilisez /holo addline la prochaine fois pour ajouter une ligne !"
						: "Ligne inseré entre la ligne " + insertAfter + " et " + (insertAfter + 1) + "!"));

	}

	@Override
	protected final String getHelp() {
		return "Grace à cet argument vous pourrez inserer une ligne avec du texte ou non à un certain emplacement sur un hologramme.";
	}

	@Override
	protected final List<String> getSubArguments() {
		return NamedHologramManager.getHologramsNames();
	}

}
