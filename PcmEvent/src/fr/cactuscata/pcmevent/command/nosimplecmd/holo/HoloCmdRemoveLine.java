package fr.cactuscata.pcmevent.command.nosimplecmd.holo;

import java.util.List;

import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SubCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.hologram.objects.NamedHologram;
import fr.cactuscata.pcmevent.utils.hologram.objects.NamedHologramManager;

/**
 * Cette classe permet l'utilisation de l'argument <code>'removeline'</code> de
 * la commande <code>'/holo'</code>.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see {@link HoloCmd}
 */

final class HoloCmdRemoveLine extends SubCommand {

	/**
	 * D�finis les valeurs par d�faut pour sa super-classe {@link SubCommand}.
	 */
	HoloCmdRemoveLine() {
		super("remove", SenderType.ALL, "Veuillez pr�ciser le nom de l'hologramme !",
				"Veuillez pr�ciser la ligne en question !");
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final NamedHologram hologram = NamedHologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.isNull(hologram, "L'hologramme '" + args[0] + "' n'existe pas !");

		final int lineNumber = CommandValidator.getInt(args[1]);

		CommandValidator.isBetweenTo(lineNumber, 1, hologram.size(),
				"La ligne sp�cifi� doit �tre entre 1 et " + hologram.size() + ".");
		CommandValidator.isInferiorTo(hologram.size(), 2,
				"Il ne reste plus qu'une ligne � l'hologramme, si vous voulez le supprimer utlisez la commande /holo delete.");
		hologram.removeLine(lineNumber - 1);
		hologram.refreshAll();

		sender.sendMessage(PrefixMessage.PREFIX + "La ligne " + lineNumber + " a �t� retir�e !");
	}

	@Override
	protected final String getHelp() {
		return "Grace � cet argument vous pourrez retirer la ligne pr�cis�e d'un hologramme.";
	}

	@Override
	protected final List<String> getSubArguments() {
		return NamedHologramManager.getHologramsNames();
	}

}
