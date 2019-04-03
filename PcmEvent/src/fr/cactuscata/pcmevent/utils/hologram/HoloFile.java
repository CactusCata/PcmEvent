package fr.cactuscata.pcmevent.utils.hologram;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import fr.cactuscata.pcmevent.utils.bukkit.LocationSerializer;
import fr.cactuscata.pcmevent.utils.bukkit.file.FileUtils;
import fr.cactuscata.pcmevent.utils.hologram.objects.CraftHologram;
import fr.cactuscata.pcmevent.utils.hologram.objects.NamedHologram;
import fr.cactuscata.pcmevent.utils.hologram.objects.NamedHologramManager;
import fr.cactuscata.pcmevent.utils.hologram.objects.lines.CraftHologramLine;
import fr.cactuscata.pcmevent.utils.hologram.objects.lines.CraftTextLine;

/**
 * Cette classe permet d'initialiser tous les hologrammes depuis le fichier de
 * configuration.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 *
 */

public final class HoloFile extends FileUtils {

	private static final long serialVersionUID = 1L;

	public HoloFile() {
		super("holograms.yml");
		super.init();
	}

	@Override
	protected final void init(final FileConfiguration config) {
		for (final String key : config.getKeys(false)) {

			final ConfigurationSection section = config.getConfigurationSection(key);

			final NamedHologram holo = new NamedHologram(
					LocationSerializer.locationFromString(section.getString("location")), key);
			section.getStringList("lines").forEach(line -> holo.getLinesUnsafe().add(readLineFromString(line, holo)));
			NamedHologramManager.addHologram(holo);

		}

	}

	public final CraftHologramLine readLineFromString(final String rawText, final CraftHologram hologram) {
		return rawText.trim().equalsIgnoreCase("{empty}") ? new CraftTextLine(hologram, "")
				: new CraftTextLine(hologram, rawText.replace('&', '§'));
	}

	public final String saveLineToString(final CraftHologramLine line) {
		return line instanceof CraftTextLine ? ((CraftTextLine) line).getText().replace('§', '&') : "Unknown";
	}

	@Override
	protected final void updateFile(final FileConfiguration config) {
		for (final NamedHologram holo : NamedHologramManager.getHolograms()) {
			final ConfigurationSection configSection = config.getConfigurationSection(holo.getName());

			configSection.set("location", LocationSerializer.locationToString(holo.getLocation()));
			final List<String> lines = new ArrayList<>();

			holo.getLinesUnsafe().forEach(line -> lines.add(saveLineToString(line)));

			configSection.set("lines", lines);
		}

	}

	public final void reload() {
		super.update();
		super.init();
	}

}
