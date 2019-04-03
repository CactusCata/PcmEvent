package fr.cactuscata.pcmevent.utils.bukkit;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.common.collect.ImmutableList;

import fr.cactuscata.pcmevent.utils.sql.Sql;

/**
 * <p>
 * Cette classe permet la récupéraion de l'uuid d'un joueur grace à un pseudo
 * ({@link UUIDFetcher#getUUIDOf(String)}), utilisé principalement pour les
 * joueurs déconnectés.
 * </p>
 * <p>
 * Elle était anciennement utilisé pour la gestion des fichier et est maintenant
 * utilisé pour {@link Sql} la bases de données.
 * </p>
 * <p>
 * <a href="https://gist.github.com/evilmidget38/26d70114b834f71fb3b4"> Site
 * d'où est tiré la classe.</a>
 * </p>
 * 
 * @author Nate Mortensen
 * @version 2.5.1
 * @since 1.1.0
 *
 */

public final class UUIDFetcher implements Callable<Map<String, UUID>> {
	private static final double PROFILES_PER_REQUEST = 100;
	private static final String PROFILE_URL = "https://api.mojang.com/profiles/minecraft";
	private final JSONParser jsonParser = new JSONParser();
	private final List<String> names;

	private UUIDFetcher(final List<String> names) {
		this.names = ImmutableList.copyOf(names);
	}

	@Override
	public final Map<String, UUID> call() throws Exception {
		final Map<String, UUID> uuidMap = new HashMap<String, UUID>();
		final int requests = (int) Math.ceil(names.size() / PROFILES_PER_REQUEST);
		for (int i = 0; i < requests; i++) {
			final HttpURLConnection connection = createConnection();
			final String body = JSONArray.toJSONString(names.subList(i * 100, Math.min((i + 1) * 100, names.size())));
			writeBody(connection, body);
			final JSONArray array = (JSONArray) jsonParser.parse(new InputStreamReader(connection.getInputStream()));
			for (final Object profile : array) {
				final JSONObject jsonProfile = (JSONObject) profile;
				final String id = (String) jsonProfile.get("id"), name = (String) jsonProfile.get("name");
				final UUID uuid = UUIDFetcher.getUUID(id);
				uuidMap.put(name, uuid);
			}

		}
		return uuidMap;
	}

	private static final void writeBody(final HttpURLConnection connection, final String body) throws Exception {
		final OutputStream stream = connection.getOutputStream();
		stream.write(body.getBytes());
		stream.flush();
		stream.close();
	}

	private static final HttpURLConnection createConnection() throws Exception {
		final URL url = new URL(PROFILE_URL);
		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		return connection;
	}

	private static final UUID getUUID(final String id) {
		return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-"
				+ id.substring(16, 20) + "-" + id.substring(20, 32));
	}

	public static final byte[] toBytes(final UUID uuid) {
		final ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
		byteBuffer.putLong(uuid.getMostSignificantBits());
		byteBuffer.putLong(uuid.getLeastSignificantBits());
		return byteBuffer.array();
	}

	public static final UUID fromBytes(final byte[] array) {
		if (array.length != 16)
			throw new IllegalArgumentException("Illegal byte array length: " + array.length);

		final ByteBuffer byteBuffer = ByteBuffer.wrap(array);
		final long mostSignificant = byteBuffer.getLong(), leastSignificant = byteBuffer.getLong();
		return new UUID(mostSignificant, leastSignificant);
	}

	public static final UUID getUUIDOf(final String playerName) {
		try {
			return new UUIDFetcher(Arrays.asList(playerName)).call().get(playerName);
		} catch (Exception e) {
			return null;
		}
	}

}