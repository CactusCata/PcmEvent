package fr.cactuscata.pcmevent.utils.experience;

import java.util.UUID;

import fr.cactuscata.pcmevent.utils.sql.TableEventPlayersSql;
import fr.cactuscata.pcmevent.utils.sql.TableEventPlayersSql.SqlColumn;

/**
 * Cette classe permet de gerer l'experience ainsi que les niveaux de joueurs
 * d�connect�s.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see Experience ExperienceClassementSystem
 *
 */
public final class ExperienceManager {

	/**
	 * M�thode qui permet d'ajouter de l'experience � un joueur d�connect�.
	 * 
	 * @param uuid
	 *            {@link UUID} du joueur.
	 * @param experienceAdded
	 *            Experience ajout�.
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
	 * Permet de retirer de l'experience � un joueur.
	 * 
	 * 
	 * @param uuid
	 *            {@link UUID} du joueur cible.
	 * @param experienceRemoved
	 *            Experience retir�.
	 */
	public static final void removeExperienceOfflinePlayer(final UUID uuid, final long experienceRemoved) {
		setExperienceOfflinePlayer(uuid, -experienceRemoved);
	}

	/**
	 * Permet de reset l'experience d'un joueur d�oonnct�.
	 * 
	 * @param uuid
	 *            {@link UUID} du joueur cible.
	 */
	public static final void resetExperienceOfflinePlayer(final UUID uuid) {
		setExperienceOfflinePlayer(uuid, 0);
	}

	/**
	 * Pemet de mettre � jour l'experience du joueur d�connect� dans la base de
	 * donn�e.
	 * 
	 * @param uuid
	 *            {@link UUID} du joueur cible
	 * @param experience
	 *            experience fix� dans la base de donn�e.
	 */
	public static final void setExperienceOfflinePlayer(final UUID uuid, final long experience) {
		TableEventPlayersSql.getTableEventInstance().updateStatsSql(uuid, SqlColumn.EXPERIENCE, experience);
	}

	/**
	 * Permet ajouter des niveaux � un joueur d�connect�.
	 * 
	 * @param uuid
	 *            {@link UUID} du joueur cible.
	 * @param level
	 *            Niveaux ajout�s.
	 */
	public static final void addLevelOffinePlayer(final UUID uuid, final int level) {
		setLevelOfflinePlayer(uuid, level);
	}

	/**
	 * Permet de retirer un certain nombre de niveau � un joueur d�connect�.
	 * 
	 * @param uuid
	 *            {@link UUID} du joueur cible.
	 * @param level
	 *            Niveaux retir�s.
	 */
	public static final void removeLevelOfflinePlayer(final UUID uuid, final int level) {
		setLevelOfflinePlayer(uuid, -level);
	}

	/**
	 * Permet de mettre � 1 le niveau du joueur d�connect�.
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
	 * �Permet de r�cuperer l'affchage de la partie niveau c'est � dire
	 * <code>�a�b[� code couleur + le niveau �a�b]</code>.
	 * 
	 * @param level
	 *            Niveau sp�cifi� pour avoir la synthaxe du code couleur.
	 * @return La synthaxe du niveau.
	 */
	public final static String getSyntaxeLevelTab(final int level) {
		return "�a�b[�"
				+ (level < 109 ? ExperienceClassementSystem.getLevelsColors()[(int) level / 10]
						: ExperienceClassementSystem
								.getLevelsColors()[ExperienceClassementSystem.getLevelsColors().length - 1])
				+ level + "�a�b]";
	}

}
