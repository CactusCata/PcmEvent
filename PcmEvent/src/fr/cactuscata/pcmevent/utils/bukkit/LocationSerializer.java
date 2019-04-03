package fr.cactuscata.pcmevent.utils.bukkit;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * You can, with this class, serialize a {@link String} in a
 * {@link org.bukkit.Location} and vice versa.
 * 
 * @author CactusCata
 */

public final class LocationSerializer {
	private static final DecimalFormat decimalFormat = new DecimalFormat("0.000");

	static {
		final DecimalFormatSymbols formatSymbols = decimalFormat.getDecimalFormatSymbols();
		formatSymbols.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(formatSymbols);
	}

	/**
	 * Convert {@link String} to {@link org.bukkit.Location}.
	 * 
	 * @param input
	 *            sentence {@link String} to serialize.
	 * @return The {@link org.bukkit.Location}.
	 */
	public static Location locationFromString(final String input) {

		final String[] parts = input.split(",");
		return new Location(Bukkit.getWorld(parts[0].trim()), Double.parseDouble(parts[1].replace(" ", "")),
				Double.parseDouble(parts[2].replace(" ", "")), Double.parseDouble(parts[3].replace(" ", "")),
				parts.length > 4 ? Float.parseFloat(parts[4].replace(" ", "")) : 0,
				parts.length > 5 ? Float.parseFloat(parts[5].replace(" ", "")) : 0);

	}

	/**
	 * Convert {@link org.bukkit.Location} to {@link String}.
	 * 
	 * @param location
	 *            The {@link org.bukkit.Location} specified.
	 * @return The {@link String}.
	 */
	public static String locationToString(final Location location) {
		return location.getWorld().getName() + ", " + decimalFormat.format(location.getX()) + ", "
				+ decimalFormat.format(location.getY()) + ", " + decimalFormat.format(location.getZ());
	}
}