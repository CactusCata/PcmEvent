package fr.cactuscata.pcmevent.command.nosimplecmd.permissions;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.SubCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.other.StringUtils;
import fr.cactuscata.pcmevent.utils.permissions.PermissionsFile;

final class PermissionsCmdList extends SubCommand {

	PermissionsCmdList() {
		super("list", SenderType.ALL);
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		sender.sendMessage(PrefixMessage.PREFIX + StringUtils.join(
				Arrays.asList(StringUtils
						.toStringArray(PermissionsFile.getPermissionFileInstance().getGroups().values().toArray())),
				", "));
	}

	@Override
	protected final String getHelp() {
		return "Cette commande permet de récuperer la liste de tous les groupes actuels.";
	}

}
