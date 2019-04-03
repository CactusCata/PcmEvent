package fr.cactuscata.pcmevent.command.nosimplecmd.holo;

import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SubCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.hologram.objects.NamedHologram;
import fr.cactuscata.pcmevent.utils.hologram.objects.NamedHologramManager;

/**
 * Cette classe permet l'utilisation de l'argument <code>'list'</code> de la
 * commande <code>'/holo'</code>.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see {@link HoloCmd}
 */

final class HoloCmdList extends SubCommand {

	/**
	 * Définis les valeurs par défaut pour sa super-classe {@link SubCommand}.
	 */
	HoloCmdList() {
		super("list", SenderType.ALL);
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final int page = args.length > 0 ? CommandValidator.getInt(args[0]) : 1;

		CommandValidator.isTrue(page < 1, "La page précisé doit être entre 1 ou supérieur !");

		int totalPages = NamedHologramManager.size() / 10;
		if (NamedHologramManager.size() % 10 != 0)
			totalPages++;

		CommandValidator.isTrue(NamedHologramManager.size() == 0, "Aucun hologramme n'a été crée pour l'instant !");

		sender.sendMessage(PrefixMessage.PREFIX + "Liste des hologrammes (Page " + page + " / " + totalPages + ")");
		final int fromIndex = (page - 1) * 10, toIndex = fromIndex + 10;

		for (int i = fromIndex; i < toIndex; i++) {
			if (i < NamedHologramManager.size()) {
				final NamedHologram hologram = NamedHologramManager.get(i);
				sender.sendMessage(String.format("§e - §1 %s à x: %d, y: %d, z: %d, (lines: %d, world: %s \"à ",
						hologram.getName(), hologram.getX(), hologram.getY(), hologram.getZ(), hologram.size(),
						hologram.getWorld().getName()));
			}
		}
		if (page < totalPages)
			sender.sendMessage(
					PrefixMessage.PREFIX + "Regardez la page suivante avec la commande /holo list " + (page + 1));

	}

	@Override
	protected final String getHelp() {
		return "Grace à cet argument vous pourrez récuperer la liste de tous les hologrammes ainsi que leurs coordonées.";
	}

}
