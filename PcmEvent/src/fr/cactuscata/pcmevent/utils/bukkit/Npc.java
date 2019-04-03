package fr.cactuscata.pcmevent.utils.bukkit;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.mojang.authlib.GameProfile;

import fr.cactuscata.pcmevent.utils.experience.ExperienceManager;
import fr.cactuscata.pcmevent.utils.other.Reflection;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

/**
 * <p>
 * Cette classe permet de créer des Npcs de manière simple et rapide, avec le
 * chargement des skin des joueurs.
 * </p>
 * <p>
 * Cette dernière est utilisé pour l'instant seulement pour la classe
 * {@link ExperienceManager}.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.4.0
 * @see ExperienceManager
 */

public final class Npc {

	private final GameProfile gameProfile;
	private final Location location;
	private final int entityID;

	/**
	 * Constructeur qui créer un Npc custom.
	 * 
	 * @param uuid
	 *            {@link UUID} du joueur.
	 * @param playerName
	 *            Nom du joueur sous la forme de {@link String}.
	 * @param location
	 *            Zone d'apparition.
	 */
	public Npc(final UUID uuid, final String playerName, final Location location) {
		this.entityID = (int) Math.ceil(Math.random() * 1000) + 2000;
		this.gameProfile = ((CraftServer) Bukkit.getServer()).getServer().aD()
				.fillProfileProperties(new GameProfile(uuid, playerName), true);
		this.location = location;
	}

	/**
	 * Méthode qui permet de faire spawn le Npc pour un joueur qui viens de se
	 * connecter.
	 * 
	 * @param plugin
	 *            Pour le runTask.
	 * @param player
	 *            Le joueur à qui il faut envoyer les packets.
	 */
	public final void spawn(final Plugin plugin, final Player player) {

		/*
		 * connec.sendPacket(new
		 * PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, np));
		 * connec.sendPacket(new PacketPlayOutNamedEntitySpawn(np));
		 */

		final PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
		Reflection.setValue(packet, "a", entityID);
		Reflection.setValue(packet, "b", this.gameProfile.getId());
		Reflection.setValue(packet, "c", (int) MathHelper.floor(location.getX() * 32.0D));
		Reflection.setValue(packet, "d", (int) MathHelper.floor(location.getY() * 32.0D));
		Reflection.setValue(packet, "e", (int) MathHelper.floor(location.getZ() * 32.0D));
		Reflection.setValue(packet, "f", (byte) ((int) (location.getYaw() / 360.0F)));
		Reflection.setValue(packet, "g", (byte) ((int) (location.getPitch() / 360.0F)));
		Reflection.setValue(packet, "h", 0);

		final DataWatcher w = new DataWatcher(null);
		w.a(6, (float) 20);
		w.a(10, (byte) 127);
		Reflection.setValue(packet, "i", w);
		addToTablist(player);
		Reflection.sendPacket(packet, player);
		Bukkit.getScheduler().runTask(plugin, new Runnable() {

			@Override
			public final void run() {
				final String playerName = Npc.this.gameProfile.getName();
				boolean b = false;
				for (final Player player : Bukkit.getOnlinePlayers()) {
					if (player.getName().equals(playerName)) {
						b = true;
						break;
					}
				}
				if (!b)
					rmvFromTablist();
			}
		});
	}

	/**
	 * Permet d'ajouter qui puis de retirer plus tard le Npc du tab.
	 * 
	 * @param player
	 *            ajout du joueur pour le joueur.
	 */
	private final void addToTablist(final Player player) {

		final PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
		final PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(this.gameProfile, 1,
				EnumGamemode.CREATIVE, CraftChatMessage.fromString(this.gameProfile.getName())[0]);
		@SuppressWarnings("unchecked")
		List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) Reflection
				.getValue(packet, "b");
		players.add(data);

		Reflection.setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
		Reflection.setValue(packet, "b", players);
		Reflection.sendPacket(packet, player);
	}

	/**
	 * Méthode qui fait spawn tous les Nps au lancement du plugin.
	 * 
	 * @param plugin
	 *            Pour run task.
	 */
	public final void spawn(final Plugin plugin) {

		PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
		Reflection.setValue(packet, "a", entityID);
		Reflection.setValue(packet, "b", this.gameProfile.getId());
		Reflection.setValue(packet, "c", (int) MathHelper.floor(location.getX() * 32.0D));
		Reflection.setValue(packet, "d", (int) MathHelper.floor(location.getY() * 32.0D));
		Reflection.setValue(packet, "e", (int) MathHelper.floor(location.getZ() * 32.0D));
		Reflection.setValue(packet, "f", (byte) ((int) (location.getYaw() / 360.0F)));
		Reflection.setValue(packet, "g", (byte) ((int) (location.getPitch() / 360.0F)));
		Reflection.setValue(packet, "h", 0);

		DataWatcher w = new DataWatcher(null);
		w.a(6, (float) 20);
		w.a(10, (byte) 127);
		Reflection.setValue(packet, "i", w);
		addToTablist();
		Reflection.sendPacket(packet);

		Bukkit.getScheduler().runTask(plugin, new Runnable() {

			@Override
			public void run() {
				String playerName = Npc.this.gameProfile.getName();
				boolean b = false;
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player.getName().equals(playerName)) {
						b = true;
						break;
					}
				}
				if (!b)
					rmvFromTablist();
			}
		});

	}

	/**
	 * Méthode qui retire un Npc du server.
	 */
	public final void destroy() {
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { entityID });
		rmvFromTablist();
		Reflection.sendPacket(packet);
	}

	/**
	 * Méthode qui ajoute un Npc.
	 */
	private final void addToTablist() {

		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
		PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(this.gameProfile, 1,
				EnumGamemode.CREATIVE, CraftChatMessage.fromString(this.gameProfile.getName())[0]);
		@SuppressWarnings("unchecked")
		List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) Reflection
				.getValue(packet, "b");
		players.add(data);

		Reflection.setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
		Reflection.setValue(packet, "b", players);
		Reflection.sendPacket(packet);
	}

	/**
	 * Méthode qui retire un Npc du tab.
	 */
	private final void rmvFromTablist() {

		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
		PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(this.gameProfile, 1,
				EnumGamemode.CREATIVE, CraftChatMessage.fromString(this.gameProfile.getName())[0]);
		@SuppressWarnings("unchecked")
		List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) Reflection
				.getValue(packet, "b");
		players.add(data);

		Reflection.setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
		Reflection.setValue(packet, "b", players);

		Reflection.sendPacket(packet);
	}

}
