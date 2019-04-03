package fr.cactuscata.pcmevent.command.nosimplecmd.holo;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
 * Cette classe permet l'utilisation de l'argument <code>'create'</code> de la
 * commande <code>'/holo'</code>.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see {@link HoloCmd}
 */

final class HoloCmdCreate extends SubCommand {

	private final HoloFile holoFile;

	/**
	 * Définis les valeurs par défaut pour sa super-classe {@link SubCommand}.
	 * 
	 * @param holoFile
	 *            Permet d'ajouter un hologramme.
	 */
	HoloCmdCreate(final HoloFile holoFile) {
		super("create", SenderType.PLAYER, "Veuillez préciser le nom de l'hologramme !");
		this.holoFile = holoFile;
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final String name = args[0].toLowerCase();

		CommandValidator.isTrue(!name.matches("[a-zA-Z0-9_\\-]+"),
				"Le nom de l'hologramme ne peut contenir que des lettres alphanumériques !");

		CommandValidator.isTrue(NamedHologramManager.isExistingHologram(name),
				"L'hologramme '" + args[0] + "' existe déjà !");

		final Location spawnLoc = ((Player) sender).getLocation();

		spawnLoc.add(0.0D, 1.2D, 0.0D);

		final NamedHologram hologram = new NamedHologram(spawnLoc, name);
		NamedHologramManager.addHologram(hologram);

		if (args.length > 1) {
			final String text = StringUtils.join(args, 1, " ");
			CommandValidator.isTrue(text.equalsIgnoreCase("{empty}"), "La première ligne ne peut pas être vide !");

			hologram.getLinesUnsafe().add(this.holoFile.readLineFromString(text, hologram));
		} else {
			hologram.appendTextLine("Changez cette ligne avec /holo setline " + hologram.getName() + " <text>");
		}

		hologram.refreshAll();

		sender.sendMessage(PrefixMessage.PREFIX + "Vous avez créée l'hologramme '" + hologram.getName() + "'.");
	}

	@Override
	protected final String getHelp() {
		return "Grace à cette argument vous créer des hologrammes.";
	}

}
