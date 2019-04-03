package fr.cactuscata.pcmevent.utils.bukkit;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import fr.cactuscata.pcmevent.PcmEvent;

/**
 * <p>
 * Cette classe permet le fonctionnement correct d'un plugin Bungee qui utilise
 * la fonction de téléportation intra-serveur, grace à cette classe, nous
 * pouvons donc téléporter le joueur au coordonées exactes du joueur précisé.
 * </p>
 * 
 * @author CactusCata
 * @version 2.4.1
 * @since 2.0.0
 * @see PluginMessageListener
 */

public final class BungeeManager implements PluginMessageListener {

	/**
	 * Constructeur qui permet d'enregistrer la récupeation du channel du Bungee.
	 */
	public BungeeManager() {
		final Plugin plugin = PcmEvent.getPlugin();
		plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BUtils", this);
	}

	@Override
	public final void onPluginMessageReceived(final String channel, final Player player, final byte[] message) {
		if (!channel.equals("BUtils"))
			return;

		try {
			final DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
			if (in.readUTF().equals("teleportTo")) {
				final Player target = Bukkit.getPlayerExact(in.readUTF());
				if (target != null)
					player.teleport(target, TeleportCause.PLUGIN);

			}
		} catch (final IOException e) {
			System.out.println("Erreur lors de la tentative BungeeCord : " + e.getMessage());
		}
	}
}
