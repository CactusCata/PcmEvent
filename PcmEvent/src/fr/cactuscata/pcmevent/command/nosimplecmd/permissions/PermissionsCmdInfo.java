package fr.cactuscata.pcmevent.command.nosimplecmd.permissions;

import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.utils.other.StringUtils;
import fr.cactuscata.pcmevent.utils.permissions.Group;
import fr.cactuscata.pcmevent.utils.permissions.PermissionsFile;
import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SubCommand;

final class PermissionsCmdInfo extends SubCommand {

	PermissionsCmdInfo() {
		super("info", SenderType.ALL, "Veuillez préciser le nom du groupe !");
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final Group group = PermissionsFile.getPermissionFileInstance().getGroups().get(args[0]);
		CommandValidator.isNull(group, "Le groupe choisi n'existe pas !");
		sender.sendMessage(String.format("§e---------\n%s\n§e---------\n- %s", args[0],
				StringUtils.join(group.getLocalPermissions(), "\n- ")));
	}

	@Override
	protected final String getHelp() {
		return "Grace à cette commande vous pourrez récuperer les informations à propos d'un groupe en particulier.";
	}

}
