package fr.cactuscata.pcmevent.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import fr.cactuscata.pcmevent.PcmEvent;
import fr.cactuscata.pcmevent.utils.bukkit.ItemBuilder;
import fr.cactuscata.pcmevent.utils.bukkit.ParticleEffect;
import fr.cactuscata.pcmevent.utils.other.Reflection;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PlayerConnection;

/**
 * Cette classe permet de gerer l'écouteur {@link PlayerDeathEvent} qui
 * s'occupera d'un repawn automatique ainsi qu'une "âme" qui sélévera
 * légèremenent avant de disparaître.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.1
 * @see PlayerDeathEvent
 */

public final class DeathListener implements Listener {

	private final Plugin plugin = PcmEvent.getPlugin();

	/**
	 * Méthode permettant d'écouter l'evenement {@link PlayerDeathEvent}.
	 * 
	 * @param event
	 *            L'evenement en question.
	 */
	@EventHandler
	public final void onDeath(final PlayerDeathEvent event) {

		this.fakeDeath(event.getEntity());
		
		Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
	
				try {

					final EntityPlayer ep = ((CraftPlayer) event.getEntity()).getHandle();
					final PlayerConnection connection = ep.playerConnection;
					((MinecraftServer) Reflection.getField(connection.getClass(), true, "minecraftServer")
							.get(connection)).getPlayerList().moveToWorld(ep, 0, false);

				} catch (final Exception ex) {
					ex.printStackTrace();
				}
			
		}, 2L);

	}

	/**
	 * Méthode qui permet de faire le systeme d'âme.
	 * 
	 * @param player
	 *            Joueur qui vera son âme s'envoler.
	 */
	private final void fakeDeath(final Player player) {

		if (player.getNearbyEntities(50, 50, 50).stream().filter(entity -> entity instanceof Player)
				.toArray().length <= 1)
			return;

		ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.1f, 0.1f,
				0.1f, 0.05f, 200, player.getLocation(), 10);
		final ArmorStand as = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
		as.setHelmet(ItemBuilder.createSkull(player.getName()));
		as.setCustomName(player.getDisplayName());
		as.setCustomNameVisible(true);
		as.setGravity(false);
		as.setVisible(false);
		as.setSmall(true);
		as.setMarker(true);
		new BukkitRunnable() {
			private int count;

			@Override
			public final void run() {
				as.teleport(as.getLocation().add((Math.random() * 0.4) - 0.2, 0.2, (Math.random() * 0.4) - 0.2),
						TeleportCause.PLUGIN);
				this.count++;
				if (count > 30 || as.getNearbyEntities(50, 50, 50).stream().filter(entity -> entity instanceof Player)
						.toArray().length <= 1) {
					as.remove();
					super.cancel();
				}
			}
		}.runTaskTimer(this.plugin, 0L, 1L);
	}

}
