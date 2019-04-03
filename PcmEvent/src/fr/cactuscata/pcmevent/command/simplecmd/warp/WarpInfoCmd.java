package fr.cactuscata.pcmevent.command.simplecmd.warp;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.google.common.collect.Lists;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.utils.bukkit.LocationSerializer;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;

/**
 * <p>
 * Cette classe permet l'execution de la commande /warpinfo, cette dernière
 * permet de récuperer les informations d'un warp (x, y, z, yaw, pitch,
 * worldName) (gestion via l'objet {@link WarpFile}).
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.0.0
 * @see SetWarpCmd WarpCmd DelWarpCmdWarpsCmd
 */

public final class WarpInfoCmd extends SimpleCommand {

	private final WarpFile warpFile;

	/**
	 * Méthode qui permet d'instancier la super-class et permet égalemet de garder
	 * en mémoire l'objet {@link WarpFile}.
	 * 
	 * @param warpFile
	 *            Qui permettra de récuperer les informations du warp ciblé via
	 *            {@link WarpFile#getLocationWarp(String)}.
	 */
	public WarpInfoCmd(final WarpFile warpFile) {
		super(PrefixMessage.PREFIX, SenderType.ALL, "Veuillez préciser le nom warp !");
		this.warpFile = warpFile;
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		CommandValidator.isTrue(this.warpFile.notExist(args[0]), "Le warp '" + args[0] + "' n'existe pas !");
		super.sendMessage(sender, "Warp '" + args[0] + "': "
				+ LocationSerializer.locationToString(this.warpFile.getLocationWarp(args[0])));

	}

	@Override
	protected final List<String> onTabComplete(final String[] args) {
		return (args.length == 1) ? Lists.newArrayList(this.warpFile.getAllWarps().keySet()) : Arrays.asList("");
	}

	@Override
	protected final String getHelp() {
		return "Cette commande permet de récuperer les informations propres au warp précisé.";
	}
}
