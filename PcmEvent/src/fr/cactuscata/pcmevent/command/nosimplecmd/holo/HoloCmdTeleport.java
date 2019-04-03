package fr.cactuscata.pcmevent.command.nosimplecmd.holo;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SubCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.hologram.objects.NamedHologram;
import fr.cactuscata.pcmevent.utils.hologram.objects.NamedHologramManager;

/**
 * Cette classe permet l'utilisation de l'argument <code>'teleport'</code> de la
 * commande <code>'/holo'</code>.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see {@link HoloCmd}
 */

final class HoloCmdTeleport extends SubCommand {

	/**
	 * Définis les valeurs par défaut pour sa super-classe {@link SubCommand}.
	 */
	HoloCmdTeleport() {
		super("teleport", SenderType.PLAYER, "Veuillez préciser le nom de l'hologramme !");
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final NamedHologram hologram = NamedHologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.isNull(hologram, "L'hologramme '" + args[0] + "' n'existe pas !");

		((Player) sender).teleport(hologram.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
		sender.sendMessage(
				PrefixMessage.PREFIX + "Vous avez été téléporté à l'hologramme '" + hologram.getName() + "' !");
	}

	@Override
	protected final String getHelp() {
		return "Grace à cet argument vous pourrez vous téléporter à l'hologramme précisé.";
	}

	@Override
	protected final List<String> getSubArguments() {
		return NamedHologramManager.getHologramsNames();
	}

}
