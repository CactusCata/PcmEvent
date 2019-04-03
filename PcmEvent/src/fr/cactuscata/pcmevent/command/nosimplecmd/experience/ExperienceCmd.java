package fr.cactuscata.pcmevent.command.nosimplecmd.experience;

import fr.cactuscata.pcmevent.command.NotSimpleCommand;
import fr.cactuscata.pcmevent.utils.bukkit.Npc;

/**
 * Ctte classe permet la séparation des ddifférents arguments tel que
 * <code>'experience'</code>, pour modifier l'experience d'un joueur,
 * <code>'level'</code> pour modifier les levels d'un joueur ainsi que
 * l'argument <code>'update'</code> pour mettre à jour les statistiques du
 * joueur et ainsi faire réaparaitre les {@link Npc}.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.4.0
 * @see ExperienceCmdExperience ExperienceCmdLevel ExperienceCmdUpdate
 */

public final class ExperienceCmd extends NotSimpleCommand {

	/**
	 * Permet l'instanciation de tous les arguments ( {@link ExperienceCmdLevel},
	 * {@link ExperienceCmdExperience} et {@link ExperienceCmdUpdate}).
	 * 
	 * @param plugin
	 *            Pour l'argument {@link ExperienceCmdUpdate} pour faire de
	 *            l'asyncrhrone.
	 */
	public ExperienceCmd() {
		super(new ExperienceCmdLevel(), new ExperienceCmdExperience(), new ExperienceCmdUpdate());
	}

	@Override
	protected final String getTutorialCommand() {
		return "Cette commande permet d'ajouter/supprimer/mettre/remettre à zéro les levels/experience du joueur précisé.";
	}

}
