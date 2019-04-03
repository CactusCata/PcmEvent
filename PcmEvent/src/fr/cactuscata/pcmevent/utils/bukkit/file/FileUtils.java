package fr.cactuscata.pcmevent.utils.bukkit.file;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import fr.cactuscata.pcmevent.PcmEvent;
import fr.cactuscata.pcmevent.command.simplecmd.vanish.VanishFile;

/**
 * Cette super-classe permet de manipuler plus simplement les fichiers de
 * configuration avec l'extension .yml.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.4.0
 * @see VanishFile WarpFile HoloFile PhysicFile
 *
 */

public abstract class FileUtils extends File {

	private static final long serialVersionUID = 1L;
	private final String basePath;
	private final FileConfiguration config;

	/**
	 * Constructeur avec comme paramètre le nom de fichier. <b>Il faut préciser
	 * l'extention</b>.
	 * 
	 * @param fileName
	 *            Nom du fichier avec l'extension .yml.
	 */
	protected FileUtils(final String fileName) {
		super(PcmEvent.getPlugin().getDataFolder().getPath(), fileName);
		RessourceSaver.save(this);
		this.config = YamlConfiguration.loadConfiguration(this);
		this.basePath = fileName.substring(0, fileName.length() - 4);
	}

	/**
	 * Méthode pour la classe qui hérite de celle-ci, elle permet d'initialiser
	 * comme le souhaite l'utilisateur les valeurs du {@link FileConfiguration}.
	 * 
	 * @param config
	 *            Ficher de configuration.
	 */
	protected abstract void init(final FileConfiguration config);

	/**
	 * Méthode qui permet de mettre à jour le ficher de {@link FileConfiguration} à
	 * jour avec les inforamtions voulu.
	 * 
	 * @param config
	 *            Ficher de configuration.
	 */
	protected abstract void updateFile(final FileConfiguration config);

	/**
	 * Permet de créer le fichier et d'initialiser les valeurs voulu avec la méthode
	 * {@link FileUtils#init(FileConfiguration)}.
	 */
	protected final void init() {
		this.init(this.config);
	}

	/**
	 * Permet de mettre le ficher à jour, utilisé dans le {@link Plugin#onDisable()}
	 * de la classe principale. Invoque ensuite la méthode pour la classe fille à
	 * cette classe pour replacer les valeurs voulu par l'utilisateur. Le fichier
	 * est finalement sauvegardé.
	 */
	public final void update() {
		this.config.set(this.basePath, null);
		this.updateFile(this.config);
		try {
			this.config.save(this);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
