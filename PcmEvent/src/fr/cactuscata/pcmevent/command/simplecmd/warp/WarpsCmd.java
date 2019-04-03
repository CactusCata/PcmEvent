package fr.cactuscata.pcmevent.command.simplecmd.warp;

import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.other.StringUtils;

/**
 * <p>
 * Cette classe permet l'execution de la commande /warps, cette dernière permet
 * d'acceder à la liste de tous les warps présent dans l'objet {@link WarpFile}.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.0.0
 * @see SetWarpCmd WarpCmd DelWarpCmd WarpInfoCmd
 */

public final class WarpsCmd extends SimpleCommand {

	private final WarpFile warpFile;

	/**
	 * Méthode qui permet d'instancier la super-class et permet égalemet de garder
	 * en mémoire l'objet {@link WarpFile}.
	 * 
	 * @param warpFile
	 *            Qui permettra de recuperer la liste de tous les warps via
	 *            {@link WarpFile#getAllWarps()}.
	 */
	public WarpsCmd(final WarpFile warpFile) {
		super(PrefixMessage.PREFIX, SenderType.ALL);
		this.warpFile = warpFile;
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		CommandValidator.isTrue(this.warpFile.getNumberWarp() == 0, "Il n'y a aucun warp existant !");
		super.sendMessage(sender,
				"Liste de tous les warps: " + StringUtils.join(this.warpFile.getAllWarps().keySet(), ", ") + ".");

	}

	@Override
	protected final String getHelp() {
		return "Cette commande permet de visualiser la liste de tous les warps présents.";
	}

}
