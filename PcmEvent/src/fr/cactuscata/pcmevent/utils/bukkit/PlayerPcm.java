package fr.cactuscata.pcmevent.utils.bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import fr.cactuscata.pcmevent.PcmEvent;
import fr.cactuscata.pcmevent.command.simplecmd.rank.RankList;
import fr.cactuscata.pcmevent.command.simplecmd.vanish.VanishCmd;
import fr.cactuscata.pcmevent.listener.JoinGameListener;
import fr.cactuscata.pcmevent.listener.LeaveGameListener;
import fr.cactuscata.pcmevent.utils.experience.Experience;
import fr.cactuscata.pcmevent.utils.experience.ExperienceManager;
import fr.cactuscata.pcmevent.utils.permissions.Group;
import fr.cactuscata.pcmevent.utils.permissions.PermissionsFile;
import fr.cactuscata.pcmevent.utils.sql.Sql;
import fr.cactuscata.pcmevent.utils.sql.TableEventPlayersSql;
import fr.cactuscata.pcmevent.utils.sql.TableEventPlayersSql.SqlColumn;

/**
 * <p>
 * Cette classe est présente pour stocker les statistiques des joueurs, comme
 * son {@link RankList grade}, son {@link ExperienceManager experience} ou
 * encore récuperer ou mettre à jour des données dans la {@link Sql base de
 * donnée}.
 * </p>
 * <p>
 * Une {@link Map} en static ({@link PlayerPcm#playersPcm}) garde en mémoire la
 * liste de tout les joueurs connectés avec leurs statistiques. Les joueurs sont
 * automatiquement ajoutés à la map lorsque qu'ils se {@link JoinGameListener
 * connectent} et retiré lorsque ils se {@link LeaveGameListener déconnectent}.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.0.0
 * @see Sql RankList ExperienceManager
 */

public class PlayerPcm {

	private static final Map<Player, PlayerPcm> playersPcm = new HashMap<>();
	private RankList rank;
	private final Player player;
	private final Experience exp;
	private final long connectedTime = System.currentTimeMillis();
	private boolean isCheater = false;
	private final PermissionAttachment perms;

	/**
	 * Constructeur qui initilisera le {@link RankList rank}, l'{@link Experience}
	 * et ajoutera dans d'une map le paramètre player comme clef et l'instance de
	 * cette classe commande valeur.
	 * 
	 * @param player
	 *            Le joueur apparetenent à l'instance de cette classe.
	 */
	public PlayerPcm(final Player player) {
		this.player = player;
		final UUID uuid = this.player.getUniqueId();
		final TableEventPlayersSql sql = TableEventPlayersSql.getTableEventInstance();

		if (!sql.haveAccount(uuid)) {
			sql.createAccount(uuid, player.getName());
			this.rank = RankList.AUCUN;
			this.exp = new Experience(this, 1, 0);
			Bukkit.getOnlinePlayers().forEach(playerOnline -> playerOnline.sendMessage(
					PrefixMessage.PREFIX + "Veuillez souhaiter la bienvenue à §3" + player.getName() + "§e !"));
		} else {
			final ResultSet rs = sql.getLine(SqlColumn.UUID, uuid);
			this.rank = RankList.getRank(String.valueOf(sql.get(rs, SqlColumn.RANK)));
			this.exp = this.rank.isAcceptXP()
					? new Experience(this, (int) sql.get(rs, SqlColumn.LEVEL), (long) sql.get(rs, SqlColumn.EXPERIENCE))
					: new Experience(this, 1, 0L);
			try {
				rs.close();
			} catch (final SQLException e) {
				e.printStackTrace();
			}

		}

		this.perms = this.getPlayer().addAttachment(PcmEvent.getPlugin());

		PermissionsFile.getPermissionFileInstance().getGroups().get(this.getRank().getNameOfPermissionGroup())
				.addPermission(this.perms);

		this.getPermissionAttachement().getPermissions().keySet().forEach(str -> System.out.println(str));

		PlayerPcm.getPlayersPcm().put(player, this);
	}

	/**
	 * Méthode qui permet de récuperer la liste de tous les {@link PlayerPcm} comme
	 * valeur et {@link Player} comme clef.
	 * 
	 * @return La map contenant comme clef un joueur et associé son
	 *         {@link PlayerPcm}.
	 */
	public static final Map<Player, PlayerPcm> getPlayersPcm() {
		return PlayerPcm.playersPcm;
	}

	/**
	 * Méthode utilisé si un reload est fait pour éviter les erreurs.
	 */
	public static final void registerAllPlayer() {
		Bukkit.getOnlinePlayers().forEach(PlayerPcm::new);
	}

	/**
	 * Méthode invoqué lorsque un joeuur déonnecte où que le plugin se désactive.
	 * Les données de ce dernier sont mises à jour dans la base de donnée.
	 * 
	 * @param player
	 *            Le joueur à retirer.
	 * @param updateSqlStatsInAsync
	 *            update des données en Asyn.
	 */
	public static final void removePlayer(final Player player, final boolean saveSqlWithAsync) {
		final PlayerPcm playerPcm = PlayerPcm.getPlayersPcm().get(player);
		playerPcm.saveInSql(saveSqlWithAsync);
		player.removeAttachment(playerPcm.getPermissionAttachement());
		playerPcm.saveInSql(saveSqlWithAsync);
		PlayerPcm.getPlayersPcm().remove(player);
	}

	/**
	 * Métode invoqué lorsque que le plugin s'éteint, tous les joueurs voient leurs
	 * informations mises à jour dans la base de donnée.
	 * 
	 * @param updateSqlStatsInAsync
	 *            Update des données en Asyn.
	 */
	public static final void unregisterAllPlayer(final boolean updateSqlStatsInAsync) {
		Bukkit.getOnlinePlayers().forEach(player -> PlayerPcm.removePlayer(player, updateSqlStatsInAsync));
	}

	/**
	 * Récupere la joueur associé à l'instance de la classe.
	 * 
	 * @return Le {@link Player} associé l'instance de la classe.
	 */
	public final Player getPlayer() {
		return this.player;
	}

	/**
	 * Permet de récuperer le ping actuel du joueur.
	 * 
	 * @return Le ping sous forme d'int du joueur lié à l'instance de cette classe.
	 */
	public final int getPing() {
		return ((CraftPlayer) this.getPlayer()).getHandle().ping;
	}

	/**
	 * Méthode qui permet ded récuperer l'ip du joueur lié à l'instance de cette
	 * classe.
	 * 
	 * @return L'ip du joueur lié cette classe.
	 */
	public final String getIP() {
		return this.player.getAddress().getHostName();
	}

	/**
	 * Méthode qui permet de récuperer la rank du joueur associé à l'instance de
	 * cette classe.
	 * 
	 * @return Le rank du joueur.
	 */
	public final RankList getRank() {
		return this.rank;
	}

	/**
	 * Méthode qui modifie le grade du joueur.
	 * 
	 * @param staff
	 *            Le nouveau grade.
	 */
	public final void setRank(final RankList rank) {
		Map<String, Group> groups = PermissionsFile.getPermissionFileInstance().getGroups();
		Group oldGroup = groups.get(this.rank.getNameOfRank());
		if (oldGroup != null)
			oldGroup.removePermission(this.perms);
		Group newGroup = groups.get(rank.getNameOfRank());
		if (newGroup != null)
			newGroup.addPermission(this.perms);
		this.rank = rank;
		this.player.setDisplayName(this.rank.getPrefix() + this.player.getName() + this.rank.getSuffix());
	}

	/**
	 * Méthode utilisé lorsque un joueur se déconnecte du serveur, ses statistiques
	 * sont alors enregistrés dans la base de données.
	 * 
	 * @param runInAsync
	 *            Update des statistiques dans le base de donnée en async.
	 */
	public final void saveInSql(final boolean runInAsync) {
		TableEventPlayersSql.getTableEventInstance().updateSqlPlayer(this, runInAsync);
	}

	/**
	 * Méthode qui met à jour le pseudo dans le tab.
	 */
	public final void updatePlayerNameTab() {
		this.player.setPlayerListName(
				this.player.getDisplayName() + (VanishCmd.getVanished().contains(this.player) ? "§c[§fV§c]" : "")
						+ (this.rank.isAcceptXP() ? this.exp.getSyntaxeLevelTab() : ""));

	}

	/**
	 * Méthode qui permet de récuperer l'instance de la classe {@link Experience}
	 * attribué au joueur.
	 * 
	 * @return L'instance de la classe {@link Experience}.
	 */
	public final Experience getExperience() {
		return this.exp;
	}

	/**
	 * Méthode qui permet de récuperer le temps de connection.
	 * 
	 * @return Le temps de connection en millième de seconde.
	 */
	public final long getTimeConnected() {
		return System.currentTimeMillis() - this.connectedTime;
	}

	/**
	 * Méthode invoqué par le plugin d'anticheat. Permet de garder en mémoire
	 * l'instance de cette classe si le joueur se déconnecte.
	 */
	public final void setCheater() {
		this.isCheater = true;
	}

	/**
	 * Méthode invoqué par le plugin d'anticheat. Permet de garder en mémoire
	 * l'instance de cette classe si le joueur se déconnecte.
	 */
	public final void removeIsCheater() {
		this.isCheater = false;
	}

	/**
	 * Méthode invoqué par le plugin d'anticheat. Permet de garder en mémoire
	 * l'instance de cette classe si le joueur se déconnecte.
	 * 
	 * @return Vrai si le joueur est un cheater.
	 * 
	 */
	public final boolean isCheater() {
		return this.isCheater;
	}

	public final PermissionAttachment getPermissionAttachement() {
		return this.perms;
	}

	/**
	 * Méthode qui, une fois invoqué récupere la liste de tous les joueurs
	 * récuperable via la tabulation. Les joueurs en vanish sont par exemple retiré
	 * de la vue. Méthode utilidé pour les commandes.
	 * 
	 * @return La liste de joueur.
	 */
	public static final List<String> getCorrectPlayersNames() {
		return Bukkit.getOnlinePlayers().stream().filter(player -> !VanishCmd.getVanished().contains(player))
				.map(player -> player.getName()).collect(Collectors.toList());
	}

}
