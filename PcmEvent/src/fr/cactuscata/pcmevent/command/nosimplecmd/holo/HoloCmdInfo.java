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
import fr.cactuscata.pcmevent.utils.hologram.objects.lines.CraftTextLine;

/**
 * Cette classe permet l'utilisation de l'argument <code>'info'</code> de la
 * commande <code>'/holo'</code>.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see {@link HoloCmd}
 */

final class HoloCmdInfo extends SubCommand {

	private final HoloFile holoFile;

	/**
	 * Définis les valeurs par défaut pour sa super-classe {@link SubCommand}.
	 * 
	 * @param holoFile
	 *            Permet de récuperer les informations de l'hologramme spécifié.
	 */
	HoloCmdInfo(final HoloFile holoFile) {
		super("info", SenderType.ALL, "Veuillez préciser le nom de l'hologramme !");
		this.holoFile = holoFile;
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final String name = args[0].toLowerCase();
		final NamedHologram hologram = NamedHologramManager.getHologram(name);
		CommandValidator.isNull(hologram, "L'hologramme '" + args[0] + "' n'existe pas !");

		sender.sendMessage(PrefixMessage.PREFIX + "Lignes de l'hologramme '" + name + "': ");
		int index = 0;
		for (final CraftHologramLine line : hologram.getLinesUnsafe())
			sender.sendMessage(String.format("§7 %d.§e %s", ++index,
					((line instanceof CraftTextLine) ? ((CraftTextLine) line).getText()
							: this.holoFile.saveLineToString(line))));

	}

	@Override
	protected final String getHelp() {
		return "Grace à cette commande vous pourrez récuperer les informations d'un hologramme.";
	}

	@Override
	protected final List<String> getSubArguments() {
		return NamedHologramManager.getHologramsNames();
	}

}
