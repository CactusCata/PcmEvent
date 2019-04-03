package fr.cactuscata.pcmevent.command.nosimplecmd.physic;

import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SubCommand;
import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;

/**
 * Cette classe permet le fonctionnement de l'argument <code>'disable'</code> de
 * la commande <code>'/physic'</code>.
 * 
 * @author Cactuscata
 * @version 2.5.1
 * @since 2.5.1
 * @see {@link PhysicCmd}.
 */

final class PhysicCmdDisable extends SubCommand {

	private final PhysicFile physicFile;

	/**
	 * Définis les valeurs par défaut pour sa super-classe {@link SubCommand}.
	 * 
	 * @param physicFile
	 *            Permet de désactiver la physique via
	 *            {@link PhysicFile#disablePhysic()}.
	 */
	PhysicCmdDisable(final PhysicFile physicFile) {
		super("disable", SenderType.ALL);
		this.physicFile = physicFile;
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		CommandValidator.isTrue(!this.physicFile.isPhysicEnable(), "La physique est déjà désactivée !");
		this.physicFile.disablePhysic();
		sender.sendMessage(PrefixMessage.PREFIX + "La physique à bien été désactivée !");
	}

	@Override
	protected final String getHelp() {
		return "Grace à cet argument vous pourrez désactiver momentanément la physique du monde.";
	}

}
