package fr.cactuscata.pcmevent.command.simplecmd;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;

/**
 * <p>
 * Cette classe permet d'executer la commande /spawn, cette dernière permet au
 * joueur qui a éxecuté la commande de se faire téléporter au spawn.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.0.0
 */

public final class SpawnCmd extends SimpleCommand {

	/**
	 * Constructeur qui permet d'instancier la super-class.
	 */
	public SpawnCmd() {
		super(PrefixMessage.PREFIX, SenderType.PLAYER);
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {

		((Player) sender).teleport(new Location(Bukkit.getWorlds().get(0), -0.5d, 65.2d, -0.5d, 0.0f, 0.0f),
				TeleportCause.PLUGIN);
		super.sendMessage(sender, "Vous avez été téléporté au spawn !");
	}

	@Override
	protected final String getHelp() {
		return "Cette commande permet de se téléporter au spawn.";
	}

}
