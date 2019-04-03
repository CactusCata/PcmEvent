package fr.cactuscata.pcmevent.utils.custominventory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.mojang.authlib.GameProfile;

import fr.cactuscata.pcmevent.PcmEvent;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;

/**
 * <p>
 * Cette classe permet de stocker les inventaires de l'objet
 * {@link CustomPlayerInventory} et les enderchests de l'objet
 * {@link CustomEnderChest} des joueurs et ensuite de les récuperer.
 * </p>
 * 
 * @author CactusCata
 * @version 2.4.1
 * @since 2.1.0
 * @see CustomEnderChest
 * @see CustomPlayerInventory
 * @see fr.cactuscata.pcmevent.command.simplecmd.inventoryviewver.OpenInvCmd
 * @see fr.cactuscata.pcmevent.command.simplecmd.inventoryviewver.OpenEnderCmd
 */

public final class InventoryHelper {

	private final Map<String, CustomPlayerInventory> inventories = new HashMap<>();
	private final Map<String, CustomEnderChest> enderChests = new HashMap<>();

	public final CustomPlayerInventory getInventory(final Player player, final boolean online) {
		final String id = player.getUniqueId().toString();
		if (this.inventories.containsKey(id))
			return this.inventories.get(id);

		final CustomPlayerInventory inv = new CustomPlayerInventory(player, online);
		this.inventories.put(id, inv);
		return inv;
	}

	public final CustomEnderChest getEnderChest(final Player player, final boolean online) {
		final String id = player.getUniqueId().toString();
		if (this.enderChests.containsKey(id))
			return this.enderChests.get(id);

		CustomEnderChest inv = new CustomEnderChest(player, online);
		this.enderChests.put(id, inv);
		return inv;
	}

	@SuppressWarnings("deprecation")
	public static final OfflinePlayer matchPlayer(final Plugin plugin, final String name) {

		OfflinePlayer player = null;
		try {
			final UUID uuid = UUID.fromString(name);
			player = Bukkit.getOfflinePlayer(uuid);

			if ((player == null) || ((!player.hasPlayedBefore()) && (!player.isOnline())))
				return null;

			return player;
		} catch (IllegalArgumentException e) {
		}

		if (player != null)
			return (OfflinePlayer) player;

		if ((plugin.getServer().getOnlineMode()) && (!name.matches("[a-zA-Z0-9_]{3,16}")))
			return null;

		player = plugin.getServer().getPlayerExact(name);
		if (player != null)
			return (OfflinePlayer) player;

		player = plugin.getServer().getOfflinePlayer(name);
		if ((player != null) && (((OfflinePlayer) player).hasPlayedBefore()))
			return (OfflinePlayer) player;

		player = plugin.getServer().getPlayer(name);
		if (player != null)
			return (OfflinePlayer) player;

		int bestMatch = Integer.MAX_VALUE;
		final OfflinePlayer[] arrayOfOfflinePlayer = plugin.getServer().getOfflinePlayers();
		final int element = arrayOfOfflinePlayer.length;
		for (int i = 0; i < element; i++) {
			final OfflinePlayer offline = arrayOfOfflinePlayer[i];
			if (offline.getName() != null) {
				final int currentMatch = StringUtils.getLevenshteinDistance(name, offline.getName());
				if (currentMatch == 0)
					return offline;

				if (currentMatch < bestMatch) {
					bestMatch = currentMatch;
					player = offline;
				}
			}
		}
		return (OfflinePlayer) player;
	}

	public static final Player loadPlayer(final OfflinePlayer offline) {
		if ((offline == null) || (!offline.hasPlayedBefore()))
			return null;

		final MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();

		final EntityPlayer entity = new EntityPlayer(server, server.getWorldServer(0),
				new GameProfile(offline.getUniqueId(), offline.getName()),
				new PlayerInteractManager(server.getWorldServer(0)));

		final Player target = entity == null ? null : entity.getBukkitEntity();
		if (target != null)
			target.loadData();

		return target;
	}

	public final void setPlayerOnline(final Player player) {
		final String key = player.getUniqueId().toString();

		if (this.inventories.containsKey(key)) {
			this.inventories.get(key).setPlayerOnline(player);
			new BukkitRunnable() {
				@Override
				public final void run() {
					if (player.isOnline())
						player.updateInventory();
				}
			}.runTask(PcmEvent.getPlugin());
		}
		if (this.enderChests.containsKey(key))
			this.enderChests.get(key).setPlayerOnline(player);

	}

	public final void setPlayerOffline(final Player player) {
		final String key = player.getUniqueId().toString();

		if (this.inventories.containsKey(key))
			this.inventories.get(key).setPlayerOffline();

		if (this.enderChests.containsKey(key))
			this.enderChests.get(key).setPlayerOffline();

	}

	public final void changeWorld(final Player player) {
		final String key = player.getUniqueId().toString();

		if (this.inventories.containsKey(key)) {
			final Iterator<HumanEntity> iterator = this.inventories.get(key).getBukkitInventory().getViewers()
					.iterator();
			while (iterator.hasNext()) {
				HumanEntity human = iterator.next();
				if ((human.getWorld() != null) && (!human.getWorld().equals(player.getWorld())))
					human.closeInventory();

			}
		}
		if (this.enderChests.containsKey(key)) {
			final Iterator<HumanEntity> iterator = this.enderChests.get(key).getBukkitInventory().getViewers()
					.iterator();
			while (iterator.hasNext()) {
				HumanEntity human = iterator.next();
				if ((human.getWorld() != null) && (!human.getWorld().equals(player.getWorld()))) {
					human.closeInventory();
				}
			}
		}
	}

}
