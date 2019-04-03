package fr.cactuscata.pcmevent.command.nosimplecmd.permissions;

import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SubCommand;
import fr.cactuscata.pcmevent.utils.permissions.Group;
import fr.cactuscata.pcmevent.utils.permissions.PermissionsFile;

class PermissionsCmdAdd extends SubCommand {

	PermissionsCmdAdd() {
		super("add", SenderType.ALL, "Veuillez préciser un groupe !", "Veuilez préciser la permission à ajouter !");
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		Group group = PermissionsFile.getPermissionFileInstance().getGroups().get(args[0]);
		CommandValidator.isNull(group, "Le groupe choisi n'existe pas !");
		CommandValidator.isTrue(group.getLocalPermissions().contains(args[1]),
				String.format("La permission '%s' est déjà présente au sein du groupe %s !", args[1], args[0]));
		group.addPermission(args[1]);
	}

	@Override
	protected final String getHelp() {
		return "Cette commande permet d'ajouter une permission sur un groupe.";
	}

}
