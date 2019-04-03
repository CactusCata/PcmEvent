package fr.cactuscata.pcmevent.command.nosimplecmd.config;

import fr.cactuscata.pcmevent.command.NotSimpleCommand;
import fr.cactuscata.pcmevent.command.simplecmd.vanish.VanishFile;

/**
 * <p>
 * Cette classe regroupe le sous-argument <code>'list'</code>
 * ({@link ConfigCmdList}) qui permet d'afficher toutes les options disponibles.
 * Cette derni�re donne aussi acc�s au sous-argument <code>'change'</code>
 * ({@link ConfigCmdChange}) qui permet de modifier les valeurs pr�sentes dans
 * l'argument <code>'list'</code>. Toutes ces options sont stock�s dans l'objet
 * {@link VanishFile}.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.5.0
 * @see VanishFile ConfigCmdList ConfigCmdChange
 */

public final class ConfigCmd extends NotSimpleCommand {

	/**
	 * 
	 * @param vanishFile
	 *            Pour pouvoir instancier correctement les objets
	 *            {@link ConfigCmdList} et {@link ConfigCmdChange}.
	 */
	public ConfigCmd(final VanishFile vanishFile) {
		super(new ConfigCmdChange(vanishFile), new ConfigCmdList(vanishFile));
	}

	@Override
	protected final String getTutorialCommand() {
		return "Cette commande permet de modifier via /config change <path> une des valeurs du fichier vanish.yml. Vous pouvez r�cuperer la liste de tous les 'path' via la commande /config list.";
	}

}
