package fr.cactuscata.pcmevent.utils.custominventory;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

import fr.cactuscata.pcmevent.command.simplecmd.inventoryviewver.OpenEnderCmd;
import fr.cactuscata.pcmevent.command.simplecmd.inventoryviewver.OpenInvCmd;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;

/**
 * <p>
 * Cette classe permet des utilitaires (ouais j'sais c'est court).
 * </p>
 * 
 * @author CactusCata
 * @version 2.4.1
 * @since 2.0.0
 * @see OpenEnderCmd 
 * @see OpenInvCmd
 */

public final class PlayerDataManager {

	public final Player loadPlayer(final OfflinePlayer offline) {
		// Ensure the player has data
		if (offline == null || !offline.hasPlayedBefore())
			return null;

		// Create a profile and entity to load the player data
		final MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		final EntityPlayer entity = new EntityPlayer(server, server.getWorldServer(0),
				new GameProfile(offline.getUniqueId(), offline.getName()),
				new PlayerInteractManager(server.getWorldServer(0)));

		// Get the bukkit entity
		final Player target = (entity == null) ? null : entity.getBukkitEntity();
		if (target != null)
			target.loadData();

		// Return the entity
		return target;
	}

	public final OfflinePlayer getPlayerByID(final String identifier) {
		try {
			final OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(identifier));
			// Ensure player is a real player, otherwise return null
			if (player == null || !player.hasPlayedBefore() && !player.isOnline()) {
				return null;
			}
			return player;
		} catch (final IllegalArgumentException e) {
			// Not a UUID
			return null;
		}
	}

	public final static EntityPlayer getHandle(final Player player) {
		if (player instanceof CraftPlayer)
			return ((CraftPlayer) player).getHandle();

		final Server server = player.getServer();
		EntityPlayer nmsPlayer = null;

		if (server instanceof CraftServer)
			nmsPlayer = ((CraftServer) server).getHandle().getPlayer(player.getName());

		if (nmsPlayer == null) {
			// Could use reflection to examine fields, but it's honestly not
			// worth the bother.
			throw new RuntimeException("Unable to fetch EntityPlayer from provided Player implementation");
		}

		return nmsPlayer;
	}
}
