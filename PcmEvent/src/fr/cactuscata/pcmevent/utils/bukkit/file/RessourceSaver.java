package fr.cactuscata.pcmevent.utils.bukkit.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.google.common.base.Charsets;

import fr.cactuscata.pcmevent.PcmEvent;

public final class RessourceSaver {

	public static final void save(final File file) {
		checkIfFileExist(file);
	}

	public static final void checkIfFileExist(final File file) {
		if (!file.exists()) {
			final Configuration config = YamlConfiguration.loadConfiguration(file);
			config.setDefaults(YamlConfiguration
					.loadConfiguration(new InputStreamReader(PcmEvent.getPlugin().getResource(file.getName()), Charsets.UTF_8)));
			saveResource(file.getName());

		}
	}

	private static final void saveResource(String resourcePath) {
		if ((resourcePath == null) || (resourcePath.isEmpty()))
			throw new IllegalArgumentException("ResourcePath cannot be null or empty");

		resourcePath = resourcePath.replace('\\', '/');
		final Plugin plugin = PcmEvent.getPlugin();
		final InputStream in = plugin.getResource(resourcePath);
		if (in == null)
			throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in file");

		final int lastIndex = resourcePath.lastIndexOf('/');
		final File outFile = new File(plugin.getDataFolder(), resourcePath),
				outDir = new File(plugin.getDataFolder(), resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

		if (!outDir.exists())
			outDir.mkdirs();

		try {
			if (!outFile.exists()) {
				final OutputStream out = new FileOutputStream(outFile);
				final byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0)
					out.write(buf, 0, len);
				out.close();
				in.close();
			}
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
	}

}
