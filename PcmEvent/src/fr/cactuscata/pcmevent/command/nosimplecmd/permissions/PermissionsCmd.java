package fr.cactuscata.pcmevent.command.nosimplecmd.permissions;

import fr.cactuscata.pcmevent.command.NotSimpleCommand;

public class PermissionsCmd extends NotSimpleCommand {

	public PermissionsCmd() {
		super(new PermissionsCmdAdd(), new PermissionsCmdRemove(), new PermissionsCmdList(), new PermissionsCmdInfo());
	}

	@Override
	protected String getTutorialCommand() {
		return "Cette commande permet de modifier les caractériqtiques d'un groupe.";
	}

}
