package fr.cactuscata.pcmevent.command.simplecmd.warp;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import fr.cactuscata.pcmevent.utils.bukkit.LocationSerializer;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.bukkit.file.FileUtils;

/**
 * <p>
 * Cette classe permet tout le fonctionnement des warps comme :
 * <ul>
 * <li>{@link SetWarpCmd}</li>
 * <li>{@link WarpCmd}</li>
 * <li>{@link DelWarpCmd}</li>
 * <li>{@link WarpInfoCmd}</li>
 * <li>{@link WarpsCmd}</li>
 * </ul>
 * <p>
 * Lorsque le plugin démarre toutes les informations du fichier
 * {@code warps.yml} sont récupérés via {@link WarpFile#init()} et sont placés
 * dans l'objet {@link Map} avec comme clef l'objet {@link String} et comme
 * valeur {@link CLocation} qui permettra par la suite de savoir si le warp dans
 * lequel on veut aller se trouve dans le même monde et affiche un message
 * d'erreur si le monde est innacessible.
 * </p>
 * <p>
 * Lorsque le plugin s'éteint, toutes les informations mises dans l'object
 * {@link Map} seront écrites dans le ficher {@code warps.yml}. <strong>Cela
 * veut donc dire que si une modification est faite dans le fichier
 * {@code warps.yml} pendant que le plugin est allumé, elle ne sera pas prise en
 * compte !</strong>
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.0.0
 * @see SetWarpCmd WarpCmd DelWarpCmd WarpInfoCmd WarpsCmd CLocation.
 */

public final class WarpFile extends FileUtils {

	private static final long serialVersionUID = 1L;
	private final Map<String, CLocation> warps = new HashMap<>();

	/**
	 * Méthode qui permet d'instancier la super-class.
	 */
	public WarpFile() {
		super("warps.yml");
		super.init();
	}

	protected final void init(final FileConfiguration config) {
		config.getKeys(false).forEach(key -> this.warps.put(key,
				new CLocation(LocationSerializer.locationFromString(config.getString(key)))));
	}

	/**
	 * Teleport {@link org.bukkit.entity.Player} to warp location.
	 * 
	 * @param warpName
	 *            Name of the warp.
	 * @param player
	 *            Player
	 */
	public final void teleport(final String warpName, final Player player) {
		final CLocation cLocation = this.warps.get(warpName);
		if (!cLocation.isCorrectWorld) {
			player.sendMessage(
					PrefixMessage.PREFIX + "Le warp '" + warpName + "' donne normalement accès au monde du nom \""
							+ cLocation.worldName + "\" mais celui-ci n'est pas accèssible !");
		} else {
			player.teleport(cLocation.location, TeleportCause.PLUGIN);
			player.sendMessage(PrefixMessage.PREFIX + "Vous avez bien été téléporté au warp '" + warpName + "' !");
		}
	}

	/**
	 * Check if warp exist.
	 * 
	 * @param warpName
	 *            Used for check if the warp exist.
	 * @return The game contains a warp with this name.
	 */
	public final boolean notExist(final String warpName) {
		return this.getAllWarps().get(warpName) == null;
	}

	/**
	 * Get Location of a warp.
	 * 
	 * @param warpName
	 *            The name of the warp.
	 * @return The {@link org.bukkit.Location}.
	 */
	public final Location getLocationWarp(final String warpName) {
		return this.getAllWarps().get(warpName).location;
	}

	/**
	 * Get the name of the world form a wawrp name.
	 * 
	 * @param warpName
	 *            The name of the warp.
	 * @return The world name.
	 */
	public final String getWorldName(final String warpName) {
		return this.getAllWarps().get(warpName).worldName;
	}

	/**
	 * Add new warp.
	 * 
	 * @param warpName
	 *            The name of the new warp.
	 * @param location
	 *            The Location of the warp.
	 */
	public final void addNewWarp(final String warpName, final Location location) {
		this.getAllWarps().put(warpName, new CLocation(location));
	}

	/**
	 * Remove a warp with his name.
	 * 
	 * @param warpName
	 *            The name of the warp to delete.
	 */
	public final void removeWarp(final String warpName) {
		this.getAllWarps().remove(warpName);
	}

	/**
	 * Get all warps.
	 * 
	 * @return A {@link java.util.Map} with {@link String} like key and
	 *         {@link CLocation} with value.
	 */
	public final Map<String, CLocation> getAllWarps() {
		return this.warps;
	}

	/**
	 * Get amount of the warps.
	 * 
	 * @return amount of the warp.
	 */
	public final int getNumberWarp() {
		return this.warps.size();
	}

	public final void updateFile(final FileConfiguration config) {
		for (final String warpsNames : this.getAllWarps().keySet()) {
			final CLocation clocation = this.getAllWarps().get(warpsNames);
			config.set(warpsNames, clocation.worldName + ", " + clocation.x + ", " + clocation.y + ", " + clocation.z
					+ ", " + clocation.yaw + ", " + clocation.pitch);
		}

	}

	/**
	 * This class is a security for object {@link Location}, the world can create
	 * error if they not exist.
	 * 
	 * @author CactusCata
	 * @version 2.5.4
	 * @since 2.2.0
	 *
	 */
	private final class CLocation {
		private final String worldName;
		private final double x, y, z;
		private final float yaw, pitch;
		private final Location location;
		private final boolean isCorrectWorld;

		private CLocation(final Location location) {
			this(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(),
					location.getPitch());
		}

		private CLocation(final String worldName, final double x, final double y, final double z, final float yaw,
				final float pitch) {
			this.worldName = worldName;
			this.x = x;
			this.y = y;
			this.z = z;
			this.yaw = yaw;
			this.pitch = pitch;
			final World world = Bukkit.getWorld(worldName);
			if (world != null) {
				this.location = new Location(world, x, y, z, yaw, pitch);
				this.isCorrectWorld = true;
			} else {
				this.location = new Location(Bukkit.getWorlds().get(0), x, y, z, yaw, pitch);
				this.isCorrectWorld = false;
			}

		}

	}

}
