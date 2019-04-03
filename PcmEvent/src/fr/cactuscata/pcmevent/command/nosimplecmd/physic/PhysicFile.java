package fr.cactuscata.pcmevent.command.nosimplecmd.physic;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import fr.cactuscata.pcmevent.utils.bukkit.file.FileUtils;

public final class PhysicFile extends FileUtils {

	private static final long serialVersionUID = 1L;
	private final List<Material> blacklistedMaterials = new ArrayList<>();
	private boolean physicEnable = true;

	public PhysicFile() {
		super("physic.yml");
		super.init();
	}

	@Override
	protected final void init(final FileConfiguration config) {
		config.getStringList("list").forEach(material -> this.blacklistedMaterials.add(Material.getMaterial(material)));
	}

	@Override
	protected final void updateFile(final FileConfiguration config) {
		final ArrayList<String> materials = new ArrayList<>();

		this.blacklistedMaterials.forEach(material -> materials.add(material.toString()));

		config.set("list", materials);

	}

	public final List<Material> getList() {
		return this.blacklistedMaterials;
	}

	public final boolean isPhysicEnable() {
		return physicEnable;
	}

	public final void enablePhysic() {
		this.physicEnable = true;
	}

	public final void disablePhysic() {
		this.physicEnable = false;
	}

}
