package fr.cactuscata.pcmevent.command.simplecmd.warp;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;

/**
 * <p>
 * Cette classe permet l'execution de la commande /warp, cette dernière permet à
 * un joueur de se téléporter aux coordonnées d'un warp. Si le warp demandé à un
 * nom différent de 'event' et que le joueur n'a pas la permission
 * {@code pcm.warp}, la commande n'aboutira pas (gestion via l'objet
 * {@link WarpFile}).
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.0.0
 * @see SetWarpCmd DelWarpCmd WarpInfoCmd WarpsCmd
 */

public final class WarpCmd extends SimpleCommand {

	private final WarpFile warpFile;

	/**
	 * Méthode qui permet d'instancier la super-class et permet égalemet de garder
	 * en mémoire l'objet {@link WarpFile}.
	 * 
	 * @param warpFile
	 *            Qui permettra d'ajouter à un joueur de se téléporter aux
	 *            coordonées du waro précisé via
	 *            {@link WarpFile#teleport(String, Player)}.
	 */
	public WarpCmd(final WarpFile warpFile) {
		super(PrefixMessage.PREFIX, SenderType.PLAYER, "Veuillez préciser le warp !");
		this.warpFile = warpFile;
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		if (!args[0].equals("event") && !args[0].equals("desacoudre")
				&& !sender.hasPermission("pcm.event.command.warp.warp")) {
			super.sendMessage(sender, PrefixMessage.NOT_ENOUGHT_PERMISSION.toString());
			return;
		}
		CommandValidator.isTrue(this.warpFile.notExist(args[0]), "Le warp '" + args[0] + "' n'existe pas !");
		this.warpFile.teleport(args[0], ((Player) sender));

	}

	@Override
	protected final List<String> onTabComplete(final String[] args) {
		return (args.length == 1) ? Lists.newArrayList(this.warpFile.getAllWarps().keySet()) : Arrays.asList("");
	}

	@Override
	protected final String getHelp() {
		return "Cette commande permet se téléporter à un warp.";
	}

}
