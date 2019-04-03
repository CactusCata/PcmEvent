package fr.cactuscata.pcmevent.utils.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import fr.cactuscata.pcmevent.PcmEvent;
import fr.cactuscata.pcmevent.utils.bukkit.PlayerPcm;
import fr.cactuscata.pcmevent.utils.other.StringUtils;
import fr.cactuscata.pcmevent.utils.sql.TableEventPlayersSql.SqlColumn;

/**
 * <p>
 * Lorsque le plugin s'allume, la classe principale ({@link PcmEvent}) se
 * connecte � la base de donn�e. Cette derni�re chargera les informations des
 * joueurs qui se connecteront, principalement grace � la m�thode
 * {@link Sql#getAllInformation(UUID)} et mettra � jour les informations avec la
 * m�thode {@link Sql#updateSqlInQuit(PlayerPcm, boolean)} en asynchrone.
 * </p>
 * 
 * @author CactusCata
 * @version 2.6.0
 * @since 2.4.0
 * @see PlayerPcm SqlColumn
 */

public class Sql {

	private static Sql sqlInstance;
	private Connection connection;
	private final String tableName;

	/**
	 * @param host
	 *            Host.
	 * @param bddName
	 *            Nom de la base de donn�e.
	 * @param userName
	 *            Nom d'utilisateur.
	 * @param password
	 *            Mot de passe.
	 * @param plugin
	 *            Permet d'effectuer des mises � jour dans la base de donn�e en
	 *            Async.
	 */
	public Sql(final String tableName, final String host, final String bddName, final String userName,
			final String password) {

		this.tableName = tableName;
		Sql.sqlInstance = this;
		try {
			this.connection = DriverManager
					.getConnection("jdbc:mysql://" + host + "/" + bddName + "?autoReconnect=true", userName, password);
		} catch (final SQLException e) {
			System.out.println("Erreur lors de la tentative de connection � la bdd");
			e.printStackTrace();
		}
	}

	/**
	 * M�thode qui permet de d�connecter le plugin de la base de donn�e.
	 */
	public final void disconnect() {
		try {
			this.connection.close();
		} catch (final SQLException e) {
			if (e instanceof CommunicationsException)
				this.disconnect();
			else
				e.printStackTrace();
		}
	}

	/**
	 * M�thode qui permet de verifier si la base de donn�e contient le joueur.
	 * 
	 * @param uuid
	 *            {@link UUID} du joueur cible.
	 * @return <code>true</code> si le joueur est dans l� base de donn�e.
	 */
	public final boolean contains(final Value value) {
		try {
			final PreparedStatement ps = this.connection.prepareStatement(
					String.format("SELECT * FROM %s WHERE %s = '%s'", this.tableName, value.enumeration, value.field));
			final ResultSet rs = ps.executeQuery();
			final boolean hasAccount = rs.next();
			ps.close();
			return hasAccount;
		} catch (final SQLException e) {
			if (e instanceof CommunicationsException)
				return this.contains(value);
			return false;
		}
	}

	/**
	 * M�thode qui permet de r�cuperer les 7 joueurs qui ont le plus haut niveau.
	 * 
	 * @return Une {@link Map} qui a comme clef l'uuid du joueur et comme valeur son
	 *         niveau.
	 */
	public final Map<String, Integer> getHighScore() {
		try {

			final PreparedStatement ps = this.connection
					.prepareStatement("SELECT Level,Uuid FROM `EVENT__players` ORDER BY Level DESC LIMIT 7");
			final ResultSet res = ps.executeQuery();
			final Map<String, Integer> map = new HashMap<>();

			while (res.next())
				map.put(res.getString("Uuid"), res.getInt("Level"));
			ps.close();
			return map;
		} catch (final SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * M�thode qui permet de r�cuperer la valeur d'une des colones avec l'uuid comme
	 * compte et la {@link SqlColumn} pour r�cuperer la colone.
	 * 
	 * @param uuid
	 *            {@link UUID} du joueur.
	 * @param sqlColum
	 *            Colone de la base de donn�e.
	 * @return L'{@link Object} associ� � la colone {@link SqlColumn}.
	 */
	public final Object get(final Value key, final ISqlColumn sqlColumn) {
		try {
			final PreparedStatement ps = this.connection.prepareStatement(String.format(
					"SELECT %s FROM %s WHERE %s = '%s'", sqlColumn, this.tableName, key.enumeration, key.field));
			Object obj = null;
			final ResultSet set = ps.executeQuery();
			while (set.next())
				obj = set.getObject(sqlColumn.toString());

			ps.close();
			return obj;
		} catch (final SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * M�thode qui permet de r�cuperer la valeur sous forme d'{@link Object} avec
	 * comme la s�l�ction de colone {@link SqlColumn}.
	 * 
	 * @param resultSet
	 *            Le {@link ResultSet} obtenu via la m�thode
	 *            {@link Sql#getAllInformation(UUID)}.
	 * @param sqlColumn
	 *            La colone.
	 * @return L'objet associ� �a la valer de la colone.
	 */
	public final Object get(final ResultSet resultSet, final ISqlColumn sqlColumn) {
		try {
			return resultSet.getObject(sqlColumn.toString());
		} catch (final SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * M�thode qui permet de r�cuperer toutes les valeurs des colones associ� au
	 * compte.
	 * 
	 * @param uuid
	 *            {@link UUID} du joueur cible.
	 * @return L'�quivalent de la ligne du compte du joueur cibl�.
	 */
	public final ResultSet getLine(final ISqlColumn columnKey, final Object key) {
		try {
			final PreparedStatement ps = this.connection.prepareStatement(
					String.format("SELECT * FROM %s WHERE %s = '%s'", this.tableName, columnKey, key));
			final ResultSet rs = ps.executeQuery();
			rs.first();
			return rs;
		} catch (final SQLException e) {
			System.out.println("Erreur lors de la tentative de r�cup�ration de la liste de toutes les informations ("
					+ key.getClass().getName() + ": '" + key.toString() + "'): " + e.getMessage());
			return null;
		}
	}

	protected final void insert(Value[] values) {
		final Action action = Action.INSERT;
		try {
			final PreparedStatement ps = Sql.this.connection
					.prepareStatement(action + " INTO " + this.tableName + action.builder.build(values));
			ps.execute();
			ps.close();
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	protected final void update(boolean inAsync, Value key, Value[] values) {
		if (inAsync)
			Bukkit.getScheduler().runTaskAsynchronously(PcmEvent.getPlugin(), new Runnable() {
				@Override
				public final void run() {
					update(key, values);
				}
			});
		else
			update(key, values);
	}

	private final void update(Value key, Value[] values) {
		final Action action = Action.UPDATE;
		try {
			final PreparedStatement ps = Sql.this.connection.prepareStatement(
					String.format("%s `link`.`%s` SET %sWHERE `%s`.`%s` = '%s';", action, this.tableName,
							action.builder.build(values), this.tableName, key.enumeration, key.field.toString()));
			ps.executeUpdate();
			ps.close();
		} catch (final SQLException e) {
			if (e instanceof CommunicationsException)
				this.update(key, values);
			else
				e.printStackTrace();
		}
	}

	/**
	 * M�thode qui permet de r�cuperer l'instance de la classe {@link Sql}.
	 * 
	 * @return L'instance de la classe {@link Sql}.
	 */
	public static final Sql getSqlInstance() {
		return Sql.sqlInstance;
	}

	/**
	 * <p>
	 * Cette classe �num�re la liste des colonnes de la base de donn�e {@link Sql}
	 * disponibles.
	 * </p>
	 * 
	 * @author CactusCata
	 * @version 2.4.1
	 * @since 2.4.0
	 * @see Sql
	 */

	interface ISqlColumn {
	}

	private interface RequestBuilder {
		public String build(Value[] values);
	}

	private enum Action {
		INSERT(new RequestBuilder() {
			@Override
			public final String build(Value[] values) {

				final StringBuilder build = new StringBuilder("(");
				final String[] collums = new String[values.length];
				for (int i = 0, j = values.length; i < j; i++)
					collums[i] = '`' + values[i].enumeration.toString() + '`';
				build.append(StringUtils.join(collums, ",")).append(") VALUES (");
				for (int i = 0, j = values.length; i < j; i++)
					collums[i] = '\'' + values[i].field.toString() + '\'';
				build.append(StringUtils.join(collums, ",")).append(")");
				return build.toString();

			}

		}),

		UPDATE(new RequestBuilder() {
			@Override
			public final String build(Value[] values) {

				final String[] str = new String[values.length];
				for (int i = 0, j = values.length; i < j; i++)
					str[i] = '`' + values[i].enumeration.toString() + '`' + " = '" + values[i].field.toString() + "' ";

				return StringUtils.join(str, ", ");
			}
		});

		private final RequestBuilder builder;

		private Action(RequestBuilder builder) {
			this.builder = builder;
		}
	}

	final class Value {

		private final ISqlColumn enumeration;
		private final Object field;

		Value(ISqlColumn enumaration, Object field) {
			this.enumeration = enumaration;
			this.field = field;
		}

	}
}
