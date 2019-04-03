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
 * Cette classe permet l'utilisation de l'argument <code>'delete'</code> de la
 * commande <code>'/holo'</code>.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see {@link HoloCmd}
 */

final class HoloCmdDelete extends SubCommand {

	/**
	 * Définis les valeurs par défaut pour sa super-classe {@link SubCommand}.
	 */
	HoloCmdDelete() {
		super("delete", SenderType.ALL, "Veuillez préciser le nom de l'hologramme !");
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final NamedHologram hologram = NamedHologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.isNull(hologram, "L'hologramme '" + args[0] + "' n'existe pas !");

		hologram.delete();
		NamedHologramManager.removeHologram(hologram);
		sender.sendMessage(String.format("%sVous avez supprimé l'hologramme '%s'.", PrefixMessage.PREFIX, hologram.getName()));
	}

	@Override
	protected final String getHelp() {
		return "Grace à cet argument vous pourrez supprimer des hologrammes.";
	}

	@Override
	protected final List<String> getSubArguments() {
		return NamedHologramManager.getHologramsNames();
	}

}
