package fr.cactuscata.pcmevent.utils.permissions;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.permissions.PermissionAttachment;

import fr.cactuscata.pcmevent.utils.bukkit.PlayerPcm;

public final class Group {

	private final List<String> permissions;
	private final Group child;

	public Group(final List<String> permissions, Group child) {
		this.permissions = permissions;
		this.child = child;
	}

	public final List<String> getTotalPermissions() {
		final List<String> totalPermissions = new ArrayList<>();
		this.getAllMotherAndChilds().forEach(group -> totalPermissions.addAll(group.permissions));
		return totalPermissions;
	}

	public final List<Group> getAllMotherAndChilds() {
		final List<Group> groups = new ArrayList<>();
		groups.add(this);
		if (this.child != null)
			groups.addAll(this.child.getAllMotherAndChilds());
		return groups;
	}

	public final List<String> getLocalPermissions() {
		return this.permissions;
	}

	/**
	 * Méthode qui permet d'ajouter toutes les permissions d'un groupe à un joueur.
	 * 
	 * @param perms
	 *            Le {@link PermissionAttachment} du joueur.
	 */
	public final void addPermission(PermissionAttachment perms) {
		this.getTotalPermissions().forEach(perm -> perms.setPermission(perm, true));
	}

	/**
	 * Méthode qui permet de retirer toutes les permissions d'un groupe à un joueur.
	 * 
	 * @param perms
	 *            Le {@link PermissionAttachment} du joueur.
	 */
	public final void removePermission(PermissionAttachment perms) {
		this.getTotalPermissions().forEach(perm -> perms.unsetPermission(perm));
	}

	/**
	 * Méthode qui permet d'ajouter une permission au groupe.
	 * 
	 * @param permission
	 *            La permission.
	 */
	public final void addPermission(String permission) {
		this.getLocalPermissions().add(permission);
		this.updatePermissionsForPlayers(permission, true);
	}

	/**
	 * Méthode qui permet de retirer une permission au groupe.
	 * 
	 * @param permission
	 *            La permission.
	 */
	public final void removePermission(String permission) {
		this.getLocalPermissions().remove(permission);
		this.updatePermissionsForPlayers(permission, false);
	}

	private void updatePermissionsForPlayers(String permission, boolean add) {
		Map<String, Group> groups = PermissionsFile.getPermissionFileInstance().getGroups();
		PlayerPcm.getPlayersPcm().values().stream()
				.filter(playerPcm -> this == groups.get(playerPcm.getRank().getNameOfPermissionGroup()))
				.forEach(playerPcm -> playerPcm.getPermissionAttachement().setPermission(permission, add));
	}

}
