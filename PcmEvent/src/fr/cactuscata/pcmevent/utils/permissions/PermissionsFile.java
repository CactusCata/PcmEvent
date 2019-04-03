package fr.cactuscata.pcmevent.utils.permissions;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import fr.cactuscata.pcmevent.utils.bukkit.file.FileUtils;

public final class PermissionsFile extends FileUtils {

	private static final long serialVersionUID = 1L;
	private static PermissionsFile permissionFileInstance;
	private final Map<String, Group> groups = new HashMap<>();
	private String defaultGroupName;

	public PermissionsFile() {
		super("permissions.yml");
		super.init();
		permissionFileInstance = this;
	}

	@Override
	protected void init(FileConfiguration config) {

		ConfigurationSection groupsSection = config.getConfigurationSection("groups");
		Map<Integer, String> groups = new TreeMap<>();

		groupsSection.getKeys(false)
				.forEach(group -> groups.put(groupsSection.getInt(String.format("%s.power", group)), group));

		for (String groupName : groups.values()) {
			ConfigurationSection groupSection = groupsSection.getConfigurationSection(groupName);
			this.groups.put(groupName,
					new Group(groupSection.getStringList("permissions"),
							groupSection.getString("inheritance") == null || this.groups.get(groupName) == null ? null
									: this.groups.get(groupName)));
		}

		this.defaultGroupName = config.getString("default");

		for (Entry<String, Group> entry : this.groups.entrySet()) {
			System.out.println(String.format("%s: %s", entry.getKey(), entry.getValue().getLocalPermissions()));
		}

	}

	public final Map<String, Group> getGroups() {
		return this.groups;
	}

	@Override
	protected void updateFile(FileConfiguration config) {

	}

	public static PermissionsFile getPermissionFileInstance() {
		return permissionFileInstance;
	}

	public String getDefaultGroupName() {
		return this.defaultGroupName;
	}

}
