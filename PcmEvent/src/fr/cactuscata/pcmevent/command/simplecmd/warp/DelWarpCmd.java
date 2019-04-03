package fr.cactuscata.pcmevent.command.simplecmd.warp;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.google.common.collect.Lists;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;

/**
 * <p>
 * Cette classe permet l'utilisation de la commande /delwarp, cette dernière
 * permet de supprimer un warp existant (gestion via l'objet {@link WarpFile}).
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.0.0
 * @see SetWarpCmd WarpCmd WarpInfoCmd WarpsCmd
 */

public final class DelWarpCmd extends SimpleCommand {

	private final WarpFile warpFile;

	/**
	 * Constructeur qui permet d'instancier la super-classe.
	 * 
	 * @param warpFile
	 *            L'objet {@link WarpFile}.
	 */
	public DelWarpCmd(final WarpFile warpFile) {
		super(PrefixMessage.PREFIX, SenderType.ALL, "Veuillez préciser le nom du warp !");
		this.warpFile = warpFile;
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final StringBuilder build = new StringBuilder("Le warp '" + args[0] + "' ");
		if (this.warpFile.notExist(args[0])) {
			build.append("n'existe pas !");
		} else {
			this.warpFile.removeWarp(args[0]);
			build.append("a bien été supprimé !");
		}
		super.sendMessage(sender, build.toString());
	}

	@Override
	protected final List<String> onTabComplete(final String[] args) {
		return (args.length == 1) ? Lists.newArrayList(this.warpFile.getAllWarps().keySet()) : Arrays.asList("");
	}

	@Override
	protected final String getHelp() {
		return "Cette commande permet de supprimer un warp existant.";
	}

}
