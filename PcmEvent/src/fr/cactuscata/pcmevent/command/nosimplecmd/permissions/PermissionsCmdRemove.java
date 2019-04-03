package fr.cactuscata.pcmevent.command.nosimplecmd.permissions;

import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.utils.permissions.Group;
import fr.cactuscata.pcmevent.utils.permissions.PermissionsFile;
import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SubCommand;

final class PermissionsCmdRemove extends SubCommand {

	PermissionsCmdRemove() {
		super("remove", SenderType.ALL, "Veuillez préciser le groupe", "Veuillez préciser la permission à retirer");
	}

	@Override
	protected void execute(final CommandSender sender, final String[] args) throws CommandException {
		final Group group = PermissionsFile.getPermissionFileInstance().getGroups().get(args[0]);
		CommandValidator.isNull(group, "Le groupe choisi n'existe pas !");
		CommandValidator.isFalse(group.getLocalPermissions().contains(args[1]),
				String.format("La permission '%s' est déjà présente au sein du groupe %s !", args[1], args[0]));
		group.removePermission(args[1]);
	}

	@Override
	protected final String getHelp() {
		return "Cette commande permet de supprimer les permissions d'un groupe.";
	}

}
