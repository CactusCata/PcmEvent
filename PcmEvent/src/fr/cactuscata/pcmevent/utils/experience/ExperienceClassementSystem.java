package fr.cactuscata.pcmevent.utils.experience;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import fr.cactuscata.pcmevent.utils.bukkit.PlayerPcm;
import fr.cactuscata.pcmevent.utils.other.Maths;
import fr.cactuscata.pcmevent.utils.sql.Sql;
import fr.cactuscata.pcmevent.utils.sql.TableEventPlayersSql;
import fr.cactuscata.pcmevent.utils.sql.TableEventPlayersSql.SqlColumn;

/**
 * <p>
 * Cette classe sert à la fois d'objet (un pour chaque objet {@link PlayerPcm})
 * qui stock l'experience et le niveau et d'utilitaire avec ses méthodes static.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.4.0
 * @see PlayerPcm Npc Experience FileExperienceInfo
 */

public final class ExperienceClassementSystem {

	private static final char[] levelColor = new char[] { 'f', '7', 'e', '6', 'a', '2', '9', 'c', '4', 'd', '5' };
	private static final FileExperienceInfo[] maxScores;
	//private static final Npc[] as = new Npc[7];

	static {
		final FileExperienceInfo fileExpInfo = new FileExperienceInfo("CactusCata", 1,
				UUID.fromString("9b8d31d5-420c-4f0c-80f0-de834b737a99"));
		maxScores = new FileExperienceInfo[] { fileExpInfo, fileExpInfo, fileExpInfo, fileExpInfo, fileExpInfo,
				fileExpInfo, fileExpInfo };
	}

	/**
	 * Cette méthode permet d'initialiser et de trier les sept meilleurs joueurs en
	 * terme de niveau.
	 */
	private static final void updateScoreMap() {

		final Map<String, Integer> map = Sql.getSqlInstance().getHighScore();

		for (final String uuidString : map.keySet()) {

			final int level = map.get(uuidString);
			int rank = ExperienceClassementSystem.maxScores.length;
			for (int i = ExperienceClassementSystem.maxScores.length - 1; i > -1; i--)
				if (level > ExperienceClassementSystem.maxScores[i].level)
					rank--;

			if (maxScores.length != rank) {
				for (int maxValue = maxScores.length - 1, minValue = rank; maxValue > minValue; maxValue--)
					if (maxValue != 7)
						maxScores[maxValue] = maxScores[maxValue - 1];
				final UUID uuid = UUID.fromString(uuidString);
				ExperienceClassementSystem.maxScores[rank] = new FileExperienceInfo(
						String.valueOf(TableEventPlayersSql.getTableEventInstance().get(uuid, SqlColumn.PSEUDO)), level,
						uuid);

			}

		}

	}

	/**
	 * Méthode qui permet de mettre à jour les npc, les panneaux.
	 */
	public static final void registerExpInfo() {

		ExperienceClassementSystem.updateScoreMap();

		int valuesPassed = 0;
		final int[] allValues = { -8, -5, -3, 0, 3, 5, 8 };

		for (final int values : allValues) {

			final Location location = new Location(Bukkit.getWorlds().get(0), -1, 64, -129);
			location.setX(location.getX() + values);
			location.setZ(location.getZ() - Maths.arrondifloat(
					(float) (Math.cos(values * (values / 8)) / Math.cos((float) (values * 1.01)) + 5), 0));

			if (values == 0)
				location.setY(location.getY() + 1);

			final int correctiveValue = (int) Math.sqrt(Math.pow(values, 2));
			if (correctiveValue == 5)
				location.setZ(location.getZ() + 6);
			if (correctiveValue == 3)
				location.setZ(location.getZ() - 1);

			location.getChunk().load();

			final Block block = Bukkit.getWorlds().get(0).getBlockAt(location);
			block.setType(Material.WALL_SIGN);

			int place = (int) Maths.arrondidouble((0.13577 * Math.pow(valuesPassed, 6)
					- 2.44 * Math.pow(valuesPassed, 5) + 16.53 * Math.pow(valuesPassed, 4)
					- 52.21 * Math.pow(valuesPassed, 3) + 76.34 * Math.pow(valuesPassed, 2) - 42.35 * valuesPassed + 6),
					0);

			if (valuesPassed == 4 || valuesPassed == 5)
				place--;

			final int valueTab = place - 1;

			final Sign sign = (Sign) block.getState();
			final org.bukkit.material.Sign matSign = new org.bukkit.material.Sign(Material.WALL_SIGN);
			matSign.setFacingDirection(BlockFace.SOUTH);
			sign.setData(matSign);
			sign.setLine(0, "#" + place);
			sign.setLine(1, "§c" + maxScores[valueTab].playerName);
			sign.setLine(3,
					"level: " + "§"
							+ (maxScores[valueTab].level < 109 ? levelColor[(int) maxScores[valueTab].level / 10]
									: levelColor[levelColor.length - 1])
							+ maxScores[valueTab].level);
			sign.update();

//			location.setY(location.getY() + 1);
//			location.setX(location.getX() + 0.5);
//			location.setZ(location.getZ() - 0.5);
//			location.setDirection(new Vector(0, 3, 0));
//
//			ExperienceClassementSystem.as[valueTab] = new Npc(maxScores[valueTab].uuid, maxScores[valueTab].playerName,
//					location);

			valuesPassed++;

		}

	}

	/**
	 * Permet de faire spawn les Npc au chargement du plugin.
	 * 
	 * @param plugin
	 *            Pour la méthode {@link Npc#spawn(Plugin)}.
	 */
	// public static final void spawnNPC(final Plugin plugin) {
	//
	// for (final Npc npc : ExperienceClassementSystem.as)
	// npc.spawn(plugin);
	//
	// Bukkit.getOnlinePlayers().forEach(x ->
	// PlayerPcm.getPlayersPcm().get(x).updatePlayerNameTab());
	//
	// }

	/**
	 * Méthode pour faire spawn les Npc pour pour le joueur cible.
	 * 
	 * @param plugin
	 *            Pour la méthode {@link Npc#}.
	 * @param player
	 *            Joueur qui vera l'NPC.
	 */
	// public static final void spawnNPC(final Plugin plugin, final Player player) {
	//
	// for (final Npc npc : ExperienceClassementSystem.as)
	// npc.spawn(plugin, player);
	//
	// Bukkit.getOnlinePlayers().forEach(x ->
	// PlayerPcm.getPlayersPcm().get(x).updatePlayerNameTab());
	//
	// }

	/**
	 * Permet de supprimer tous les npc.
	 */
//	public static final void unregisterAllNPC() {
//		for (final Npc npc : ExperienceClassementSystem.as)
//			npc.destroy();
//	}

	/**
	 * Récupere la liste de tous les caractères par rapport aux niveaux.
	 * 
	 * @return La liste de codes couleurs.
	 */
	public static final char[] getLevelsColors() {
		return levelColor;
	}

	/**
	 * 
	 * @author CactusCata
	 * @version 2.5.1
	 * @since 2.4.0
	 *
	 */
	private static class FileExperienceInfo {

		private final String playerName;
		private final int level;
		@SuppressWarnings("unused")
		private final UUID uuid;

		public FileExperienceInfo(final String playerName, final int level, final UUID uuid) {
			this.playerName = playerName;
			this.level = level;
			this.uuid = uuid;
		}

	}

}
