package fr.cactuscata.pcmevent.utils.sql;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Player;

import fr.cactuscata.pcmevent.utils.bukkit.PlayerPcm;

public final class TableEventPlayersSql extends Sql {

	private static TableEventPlayersSql tableEventInstance;

	public TableEventPlayersSql(final String host, final String bddName, final String userName, final String password) {
		super("EVENT__players", host, bddName, userName, password);
		TableEventPlayersSql.tableEventInstance = this;
	}

	public final Object get(UUID uuid, SqlColumn sqlColumn) {
		return super.get(new Value(SqlColumn.UUID, uuid), sqlColumn);
	}

	public final boolean haveAccount(final UUID uuid) {
		return super.contains(new Value(SqlColumn.UUID, uuid));
	}

	/**
	 * M�thode qui permet d'ajouter un compte � la base de donn�e.
	 * 
	 * @param uuid
	 *            {@link UUID} du joueur.
	 * @param playerName
	 *            Pseudo du joueur.
	 */
	public final void createAccount(final UUID uuid, final String playerName) {
		super.insert(new Value[] { new Value(SqlColumn.UUID, uuid), new Value(SqlColumn.PSEUDO, playerName) });
	}

	/**
	 * M�thode appel� lors de la mise � jour des statistiques du joueur dans la base
	 * de donn�e.
	 * 
	 * @param playerPcm
	 *            Instance de l'objet affili� � l'instance de l'interface
	 *            {@link Player}.
	 * @param player
	 *            Instance de l'interface {@link Player}.
	 */
	public final void updateSqlPlayer(final PlayerPcm playerPcm, final boolean updateWithAsync) {

		final Player player = playerPcm.getPlayer();
		super.update(updateWithAsync, new Value(SqlColumn.UUID, player.getUniqueId()),
				new Value[] { new Value(SqlColumn.PSEUDO, player.getName()),
						new Value(SqlColumn.LAST_LOGIN, new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date())),
						new Value(SqlColumn.RANK, playerPcm.getRank().getNameOfRank()),
						new Value(SqlColumn.LEVEL, playerPcm.getExperience().getLevel()),
						new Value(SqlColumn.EXPERIENCE, playerPcm.getExperience().getExperience()) });
	}

	/**
	 * Cette m�thode permet de mettre � jour la valeur d'une des colones d'un joueur
	 * dans la base de donn�e.
	 * 
	 * @param uuid
	 *            {@link UUID} du joueur.
	 * @param sqlColumn
	 *            Colone correspondante.
	 * @param data
	 *            {@link Object} mis � jour dans la base de donn�e.
	 */
	public final void updateStatsSql(final UUID uuid, final SqlColumn sqlColumn, final Object data) {
		super.update(true, new Value(SqlColumn.UUID, uuid), new Value[] { new Value(sqlColumn, data) });
	}

	public static TableEventPlayersSql getTableEventInstance() {
		return tableEventInstance;
	}

	public static enum SqlColumn implements ISqlColumn {

		UUID(),
		PSEUDO(),
		RANK(),
		IPS(),
		FIRST_LOGIN(),
		LAST_LOGIN(),
		LEVEL(),
		EXPERIENCE();

		@Override
		public final String toString() {
			return WordUtils.capitalize(super.name().substring(0, 1).toUpperCase()
					+ super.name().substring(1).toLowerCase().replace("_", ""));
		}

	}

}
