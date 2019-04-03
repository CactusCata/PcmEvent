package fr.cactuscata.pcmevent.command.nosimplecmd.physic;

import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SubCommand;
import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;

/**
 * Cette classe permet le fonctionnement de l'argument <code>'enable'</code> de
 * la commande <code>'/physic'</code>.
 * 
 * @author Cactuscata
 * @version 2.5.1
 * @since 2.5.1
 * @see {@link PhysicCmd}.
 */

final class PhysicCmdEnable extends SubCommand {

	private final PhysicFile physicFile;

	/**
	 * Définis les valeurs par défaut pour sa super-classe {@link SubCommand}. *
	 * 
	 * @param physicFile
	 *            Prmet d'activer la physiqye via *
	 *            {@link PhysicFile#enablePhysic()}.
	 */
	PhysicCmdEnable(final PhysicFile physicFile) {
		super("enable", SenderType.ALL);
		this.physicFile = physicFile;
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		CommandValidator.isTrue(this.physicFile.isPhysicEnable(), "La physique est déjà activée !");
		this.physicFile.enablePhysic();
		sender.sendMessage(PrefixMessage.PREFIX + "La physique a bien été réactivée !");
	}

	@Override
	protected final String getHelp() {
		return "Vous pouvez grace à cet argumentn réactiver la physique du monde.";
	}

}
