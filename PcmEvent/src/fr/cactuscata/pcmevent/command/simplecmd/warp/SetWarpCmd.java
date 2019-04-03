package fr.cactuscata.pcmevent.command.simplecmd.warp;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;

/**
 * <p>
 * Cette classe permet l'utilisation de la commande /setwarp, cette dernière
 * permet de créer un warp. Si le warp existe déjà il aura pour effet d'écraser
 * l'ancien (gestion via l'objet {@link WarpFile}).
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.0.0
 * @see WarpCmd DelWarpCmd WarpInfoCmdWarpsCmd
 */

public final class SetWarpCmd extends SimpleCommand {

	private final WarpFile warpFile;

	/**
	 * Méthode qui permet d'instancier la super-class et permet égalemet de garder
	 * en mémoire l'objet {@link WarpFile}.
	 * 
	 * @param warpFile
	 *            Qui permettra d'ajouter un warp via
	 *            {@link WarpFile#addNewWarp(String, org.bukkit.Location)}.
	 */
	public SetWarpCmd(final WarpFile warpFile) {
		super(PrefixMessage.PREFIX, SenderType.PLAYER, "Veuillez préciser le nom du warp !");
		this.warpFile = warpFile;
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final StringBuilder build = new StringBuilder();
		if (args[0].contains(".")) {
			build.append("Les warps ne peuvent pas contenir de '.' !");
		} else {
			warpFile.addNewWarp(args[0], ((Player) sender).getLocation());
			build.append("Le warp " + args[0] + " a bien été ajouté !");
		}
		super.sendMessage(sender, build);
	}

	@Override
	protected final String getHelp() {
		return "Cette commande permet d'ajouter un warp.";
	}

}
