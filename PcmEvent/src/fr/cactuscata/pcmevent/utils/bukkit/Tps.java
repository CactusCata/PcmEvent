package fr.cactuscata.pcmevent.utils.bukkit;

import org.bukkit.scheduler.BukkitRunnable;

/**
 * <p>
 * Cette classe qui hérite de {@link BukkitRunnable} permet de récuperer les tps
 * moyens du monde event.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.0.0
 */

public final class Tps implements Runnable {
	private long sec, currentSec;
	private int ticks;
	private static double tps = 20.0D;

	@Override
	public final void run() {
		this.sec = (System.currentTimeMillis() / 1000L);
		if (this.currentSec == this.sec) {
			this.ticks++;
		} else {
			this.currentSec = this.sec;
			tps = tps == 0.0D ? this.ticks : (tps + this.ticks) / 2.0D;
			this.ticks = 0;
		}
	}

	/**
	 * Méthode qui invoqué récupere les tps du serveur.
	 * 
	 * @return Les tps actuels du serveur.
	 */
	public static final double getTps() {
		return tps + 1.0D > 20.0D ? 20.0D : tps + 1.0D;
	}
}
