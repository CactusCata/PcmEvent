package fr.cactuscata.pcmevent.command.simplecmd.rank;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.cactuscata.pcmevent.PcmEvent;
import fr.cactuscata.pcmevent.utils.other.DateUtils;

public final class RankFile extends File {

	private static final long serialVersionUID = 1L;

	private final FileConfiguration config = YamlConfiguration.loadConfiguration(this);

	public RankFile() {
		super(PcmEvent.getPlugin().getDataFolder().getPath(), "rank.yml");
		if (!super.exists())
			try {
				super.createNewFile();
			} catch (final IOException e) {
				e.printStackTrace();
			}
	}

	public final void write(final String playerName, final String targetName, final String rank) {
		this.config.set("[" + DateUtils.getActualDate() + "]", playerName + " --> " + targetName + " : " + rank);
		try {
			this.config.save(this);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
