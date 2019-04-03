package fr.cactuscata.pcmevent.utils.experience;

import java.util.UUID;

import fr.cactuscata.pcmevent.utils.sql.TableEventPlayersSql;
import fr.cactuscata.pcmevent.utils.sql.TableEventPlayersSql.SqlColumn;

/**
 * Cette classe permet de gerer l'experience ainsi que les niveaux de joueurs
 * déconnectés.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see Experience ExperienceClassementSystem
 *
 */
public final class ExperienceManager {

	/**
	 * Méthode qui permet d'ajouter de l'experience à un joueur déconnecté.
	 * 
	 * @param uuid
	 *            {@link UUID} du joueur.
	 * @param experienceAdded
	 *            Experience ajouté.
	 */
	public static final void addExperienceOffinePlayer(final UUID uuid, final int experienceAdded) {
		final TableEventPlayersSql tableEventPlayerSql = TableEventPlayersSql.getTableEventInstance();
		long experience = (long) tableEventPlayerSql.get(uuid, SqlColumn.EXPERIENCE);
		int level = (int) tableEventPlayerSql.get(uuid, SqlColumn.LEVEL);

		if (experience + experienceAdded <= Long.MAX_VALUE) {

			experience += experienceAdded;

			while (experience > (int) (Math.exp(Math.sqrt(level * 1.3)) + 10)) {

				experience -= ((int) Math.exp(Math.sqrt(level * 1.3)) + 10);
				level++;

			}

			setExperienceOfflinePlayer(uuid, experience);
			setLevelOfflinePlayer(uuid, level);
		}
	}

	/**
	 * Permet de retirer de l'experience à un joueur.
	 * 
	 * 
	 * @param uuid
	 *            {@link UUID} du joueur cible.
	 * @param experienceRemoved
	 *            Experience retiré.
	 */
	public static final void removeExperienceOfflinePlayer(final UUID uuid, final long experienceRemoved) {
		setExperienceOfflinePlayer(uuid, -experienceRemoved);
	}

	/**
	 * Permet de reset l'experience d'un joueur déoonncté.
	 * 
	 * @param uuid
	 *            {@link UUID} du joueur cible.
	 */
	public static final void resetExperienceOfflinePlayer(final UUID uuid) {
		setExperienceOfflinePlayer(uuid, 0);
	}

	/**
	 * Pemet de mettre à jour l'experience du joueur déconnecté dans la base de
	 * donnée.
	 * 
	 * @param uuid
	 *            {@link UUID} du joueur cible
	 * @param experience
	 *            experience fixé dans la base de donnée.
	 */
	public static final void setExperienceOfflinePlayer(final UUID uuid, final long experience) {
		TableEventPlayersSql.getTableEventInstance().updateStatsSql(uuid, SqlColumn.EXPERIENCE, experience);
	}

	/**
	 * Permet ajouter des niveaux à un joueur déconnecté.
	 * 
	 * @param uuid
	 *            {@link UUID} du joueur cible.
	 * @param level
	 *            Niveaux ajoutés.
	 */
	public static final void addLevelOffinePlayer(final UUID uuid, final int level) {
		setLevelOfflinePlayer(uuid, level);
	}

	/**
	 * Permet de retirer un certain nombre de niveau à un joueur déconnecté.
	 * 
	 * @param uuid
	 *            {@link UUID} du joueur cible.
	 * @param level
	 *            Niveaux retirés.
	 */
	public static final void removeLevelOfflinePlayer(final UUID uuid, final int level) {
		setLevelOfflinePlayer(uuid, -level);
	}

	/**
	 * Permet de mettre à 1 le niveau du joueur déconnecté.
	 * 
	 * @param uuid
	 *            {@link UUID} du joueur cible.
	 */
	public static final void resetLevelsForOfflinePlayer(final UUID uuid) {
		setLevelOfflinePlayer(uuid, 1);
	}

	public static final void setLevelOfflinePlayer(final UUID uuid, final int level) {
		TableEventPlayersSql.getTableEventInstance().updateStatsSql(uuid, SqlColumn.LEVEL, level);
	}

	/**
	 * ¨Permet de récuperer l'affchage de la partie niveau c'est à dire
	 * <code>§a§b[§ code couleur + le niveau §a§b]</code>.
	 * 
	 * @param level
	 *            Niveau spécifié pour avoir la synthaxe du code couleur.
	 * @return La synthaxe du niveau.
	 */
	public final static String getSyntaxeLevelTab(final int level) {
		return "§a§b[§"
				+ (level < 109 ? ExperienceClassementSystem.getLevelsColors()[(int) level / 10]
						: ExperienceClassementSystem
								.getLevelsColors()[ExperienceClassementSystem.getLevelsColors().length - 1])
				+ level + "§a§b]";
	}

}
