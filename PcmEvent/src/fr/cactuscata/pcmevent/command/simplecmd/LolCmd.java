package fr.cactuscata.pcmevent.command.simplecmd;

import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import net.minecraft.server.v1_8_R3.PacketPlayOutGameStateChange;

/**
 * Cette classe permet d'executer la commande <code>'/lol'</code> qui permet de
 * se marrer un bon coup !
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 */

public final class LolCmd extends SimpleCommand {

	/**
	 * Instanciation de la super-class.
	 */
	public LolCmd() {
		super(PrefixMessage.PREFIX, SenderType.PLAYER, "Veuillez préciser la force de lolitude !");
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final float amount = CommandValidator.getFloat(args[0]);
		CommandValidator.isBetweenTo(amount, 0, 100);

		((CraftPlayer) ((Player) sender)).getHandle().playerConnection
				.sendPacket(new PacketPlayOutGameStateChange(7, amount));
		super.sendMessage(sender, "Vous avez pris une énorme dose de funitude ! Bravo à vous !");

	}

	@Override
	protected String getHelp() {
		return "Cette commande permet d'obtenir un max de funitude !";
	}

}
