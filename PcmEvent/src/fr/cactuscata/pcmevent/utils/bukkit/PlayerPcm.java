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
 * Cette classe est pr�sente pour stocker les statistiques des joueurs, comme
 * son {@link RankList grade}, son {@link ExperienceManager experience} ou
 * encore r�cuperer ou mettre � jour des donn�es dans la {@link Sql base de
 * donn�e}.
 * </p>
 * <p>
 * Une {@link Map} en static ({@link PlayerPcm#playersPcm}) garde en m�moire la
 * liste de tout les joueurs connect�s avec leurs statistiques. Les joueurs sont
 * automatiquement ajout�s � la map lorsque qu'ils se {@link JoinGameListener
 * connectent} et retir� lorsque ils se {@link LeaveGameListener d�connectent}.
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
	 * et ajoutera dans d'une map le param�tre player comme clef et l'instance de
	 * cette classe commande valeur.
	 * 
	 * @param player
	 *            Le joueur apparetenent � l'instance de cette classe.
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
					PrefixMessage.PREFIX + "Veuillez souhaiter la bienvenue � �3" + player.getName() + "�e !"));
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
	 * M�thode qui permet de r�cuperer la liste de tous les {@link PlayerPcm} comme
	 * valeur et {@link Player} comme clef.
	 * 
	 * @return La map contenant comme clef un joueur et associ� son
	 *         {@link PlayerPcm}.
	 */
	public static final Map<Player, PlayerPcm> getPlayersPcm() {
		return PlayerPcm.playersPcm;
	}

	/**
	 * M�thode utilis� si un reload est fait pour �viter les erreurs.
	 */
	public static final void registerAllPlayer() {
		Bukkit.getOnlinePlayers().forEach(PlayerPcm::new);
	}

	/**
	 * M�thode invoqu� lorsque un joeuur d�onnecte o� que le plugin se d�sactive.
	 * Les donn�es de ce dernier sont mises � jour dans la base de donn�e.
	 * 
	 * @param player
	 *            Le joueur � retirer.
	 * @param updateSqlStatsInAsync
	 *            update des donn�es en Asyn.
	 */
	public static final void removePlayer(final Player player, final boolean saveSqlWithAsync) {
		final PlayerPcm playerPcm = PlayerPcm.getPlayersPcm().get(player);
		playerPcm.saveInSql(saveSqlWithAsync);
		player.removeAttachment(playerPcm.getPermissionAttachement());
		playerPcm.saveInSql(saveSqlWithAsync);
		PlayerPcm.getPlayersPcm().remove(player);
	}

	/**
	 * M�tode invoqu� lorsque que le plugin s'�teint, tous les joueurs voient leurs
	 * informations mises � jour dans la base de donn�e.
	 * 
	 * @param updateSqlStatsInAsync
	 *            Update des donn�es en Asyn.
	 */
	public static final void unregisterAllPlayer(final boolean updateSqlStatsInAsync) {
		Bukkit.getOnlinePlayers().forEach(player -> PlayerPcm.removePlayer(player, updateSqlStatsInAsync));
	}

	/**
	 * R�cupere la joueur associ� � l'instance de la classe.
	 * 
	 * @return Le {@link Player} associ� l'instance de la classe.
	 */
	public final Player getPlayer() {
		return this.player;
	}

	/**
	 * Permet de r�cuperer le ping actuel du joueur.
	 * 
	 * @return Le ping sous forme d'int du joueur li� � l'instance de cette classe.
	 */
	public final int getPing() {
		return ((CraftPlayer) this.getPlayer()).getHandle().ping;
	}

	/**
	 * M�thode qui permet ded r�cuperer l'ip du joueur li� � l'instance de cette
	 * classe.
	 * 
	 * @return L'ip du joueur li� cette classe.
	 */
	public final String getIP() {
		return this.player.getAddress().getHostName();
	}

	/**
	 * M�thode qui permet de r�cuperer la rank du joueur associ� � l'instance de
	 * cette classe.
	 * 
	 * @return Le rank du joueur.
	 */
	public final RankList getRank() {
		return this.rank;
	}

	/**
	 * M�thode qui modifie le grade du joueur.
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
	 * M�thode utilis� lorsque un joueur se d�connecte du serveur, ses statistiques
	 * sont alors enregistr�s dans la base de donn�es.
	 * 
	 * @param runInAsync
	 *            Update des statistiques dans le base de donn�e en async.
	 */
	public final void saveInSql(final boolean runInAsync) {
		TableEventPlayersSql.getTableEventInstance().updateSqlPlayer(this, runInAsync);
	}

	/**
	 * M�thode qui met � jour le pseudo dans le tab.
	 */
	public final void updatePlayerNameTab() {
		this.player.setPlayerListName(
				this.player.getDisplayName() + (VanishCmd.getVanished().contains(this.player) ? "�c[�fV�c]" : "")
						+ (this.rank.isAcceptXP() ? this.exp.getSyntaxeLevelTab() : ""));

	}

	/**
	 * M�thode qui permet de r�cuperer l'instance de la classe {@link Experience}
	 * attribu� au joueur.
	 * 
	 * @return L'instance de la classe {@link Experience}.
	 */
	public final Experience getExperience() {
		return this.exp;
	}

	/**
	 * M�thode qui permet de r�cuperer le temps de connection.
	 * 
	 * @return Le temps de connection en milli�me de seconde.
	 */
	public final long getTimeConnected() {
		return System.currentTimeMillis() - this.connectedTime;
	}

	/**
	 * M�thode invoqu� par le plugin d'anticheat. Permet de garder en m�moire
	 * l'instance de cette classe si le joueur se d�connecte.
	 */
	public final void setCheater() {
		this.isCheater = true;
	}

	/**
	 * M�thode invoqu� par le plugin d'anticheat. Permet de garder en m�moire
	 * l'instance de cette classe si le joueur se d�connecte.
	 */
	public final void removeIsCheater() {
		this.isCheater = false;
	}

	/**
	 * M�thode invoqu� par le plugin d'anticheat. Permet de garder en m�moire
	 * l'instance de cette classe si le joueur se d�connecte.
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
	 * M�thode qui, une fois invoqu� r�cupere la liste de tous les joueurs
	 * r�cuperable via la tabulation. Les joueurs en vanish sont par exemple retir�
	 * de la vue. M�thode utilid� pour les commandes.
	 * 
	 * @return La liste de joueur.
	 */
	public static final List<String> getCorrectPlayersNames() {
		return Bukkit.getOnlinePlayers().stream().filter(player -> !VanishCmd.getVanished().contains(player))
				.map(player -> player.getName()).collect(Collectors.toList());
	}

}
