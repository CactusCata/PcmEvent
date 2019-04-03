package fr.cactuscata.pcmevent.command.simplecmd.vanish;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import fr.cactuscata.pcmevent.listener.VanishListener;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.bukkit.file.FileUtils;

/**
 * <p>
 * Lorsque le plugin s'allume, l'instance de la classe se fait et execute la
 * méthode {@link VanishFile#init(FileConfiguration)} qui va mettre toutes les
 * valeurs de la config dans une {@link Map} avec comme clef {@link String} et
 * comme valeur un {@link Boolean}.
 * </p>
 * <p>
 * Une fois que le plugin s'éteint, toutes les informations mises dans la
 * {@link Map} {@link VanishFile#vanishMap} se mettent à jour dans la fichier de
 * config.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.2.0
 * @see VanishListener
 */

public final class VanishFile extends FileUtils {

	private static final long serialVersionUID = 1L;
	private final Map<String, Boolean> vanishMap = new HashMap<>();

	/**
	 * Récupeation du fichier config.yml.
	 * 
	 */
	public VanishFile() {
		super("vanish.yml");
		super.init();
	}

	protected final void init(final FileConfiguration config) {
		config.getConfigurationSection("Vanish").getKeys(false)
				.forEach(value -> this.vanishMap.put(value, config.getBoolean("Vanish." + value)));

	}

	/**
	 * Méthode qui permet de modifier l'une des valeurs du fichier de configuration.
	 * 
	 * @param infoName
	 *            Valeur sous forme de {@link String} du path de la condig.
	 * @param newValue
	 *            Nouvelle valeur boolean.
	 */
	public final void toogleValue(final String infoName) {
		this.vanishMap.put(infoName, !this.vanishMap.get(infoName));
	}

	/**
	 * Récupere la valeur d'un path du fichier de configuration.
	 * 
	 * @param infoName
	 *            Clef sous forme de {@link String}.
	 * @return La valeur boolean.
	 */
	public final boolean getValue(final String infoName) {
		return this.vanishMap.get(infoName);
	}

	protected final void updateFile(final FileConfiguration config) {
		this.vanishMap.keySet().forEach(value -> config.set("Vanish." + value, this.vanishMap.get(value)));
	}

	/**
	 * Récupere la liste de toutes les clefs et leurs valeurs sous forme de phrase.
	 * 
	 * @return La phrase.
	 */
	public final String getAllList() {
		final StringBuilder message = new StringBuilder(PrefixMessage.CONFIG + "Pous les valeurs : \n");
		this.vanishMap.keySet()
				.forEach(x -> message.append(x).append(" : ").append(this.vanishMap.get(x)).append("\n"));
		return message.toString();
	}

	/**
	 * Méthode qui permet de récuperer la liste de toutes les clefs disponibles.
	 * 
	 * @return Le tableau de clef.
	 */
	public final String[] getAllEntryMap() {
		return this.vanishMap.keySet().toArray(new String[this.vanishMap.entrySet().size()]);
	}

}
