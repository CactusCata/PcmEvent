package fr.cactuscata.pcmevent.command.nosimplecmd.holo;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SubCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.hologram.objects.NamedHologram;
import fr.cactuscata.pcmevent.utils.hologram.objects.NamedHologramManager;

/**
 * Cette classe permet l'utilisation de l'argument <code>'movehere'</code> de la
 * commande <code>'/holo'</code>.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see {@link HoloCmd}
 */

final class HoloCmdMoveHere extends SubCommand {

	/**
	 * D�finis les valeurs par d�faut pour sa super-classe {@link SubCommand}.
	 */
	HoloCmdMoveHere() {
		super("movehere", SenderType.PLAYER, "Veuillez pr�ciser le nom de l'hologramme !");
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final Player player = (Player) sender;
		final NamedHologram hologram = NamedHologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.isNull(hologram, "L'hologramme '" + args[0] + "' n'existe pas !");

		hologram.teleport(player.getLocation());
		hologram.despawnEntities();
		hologram.refreshAll();
		player.sendMessage(
				PrefixMessage.PREFIX + "Vous avez d�plac� l'hologramme '" + hologram.getName() + "' sur vous !");
	}

	@Override
	protected final String getHelp() {
		return "Grace � cette commande vous pourrez t�l�porter un hologramme � vous.";
	}

	@Override
	protected final List<String> getSubArguments() {
		return NamedHologramManager.getHologramsNames();
	}

}
