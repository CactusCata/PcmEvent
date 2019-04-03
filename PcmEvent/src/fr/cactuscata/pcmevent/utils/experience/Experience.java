package fr.cactuscata.pcmevent.utils.experience;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import fr.cactuscata.pcmevent.command.simplecmd.vanish.VanishCmd;
import fr.cactuscata.pcmevent.utils.bukkit.ParticleEffect;
import fr.cactuscata.pcmevent.utils.bukkit.PlayerPcm;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.bukkit.TitleManager;

/**
 * Cette classe permet de gerer l'experience du joueur. Chaque {@link PlayerPcm}
 * a un objet {@link Experience} qui lui est propre.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.3.0
 * @see ExperienceClassementSystem ExperienceManager
 *
 */

public final class Experience {

	private int level;
	private long experience;
	private final PlayerPcm playerPcm;

	/**
	 * Constructeur initialisant les valeurs d'experience.
	 * 
	 * @param playerPcm
	 *            Instance {@link PlayerPcm} qui est propre � celle de
	 *            {@link Experience}.
	 * @param level
	 *            Levels du joueur.
	 * @param experience
	 *            Experience du joueur.
	 */
	public Experience(final PlayerPcm playerPcm, final int level, final long experience) {
		this.playerPcm = playerPcm;
		this.level = level;
		this.experience = experience;
	}

	/**
	 * M�thode qui permet d'ajouter de l'experience.
	 * 
	 * @param experienceAdded
	 *            Experience ajout�.
	 */
	public final void addExperience(final long experienceAdded) {

		if (this.experience + experienceAdded > Long.MAX_VALUE)
			return;

		this.experience += experienceAdded;
		boolean levelChanged = false;
		final Player player = this.playerPcm.getPlayer();

		while (experience > (int) (Math.exp(Math.sqrt(this.level * 1.3)) + 10)) {

			this.experience -= ((int) Math.exp(Math.sqrt(this.level * 1.3)) + 10);
			setLevel(this.level + 1);

			if (!levelChanged)
				levelChanged = true;

			if (this.level % 10 == 0) {
				Bukkit.getOnlinePlayers().forEach(playersOnlines -> playersOnlines.sendMessage(PrefixMessage.PREFIX
						+ "Le joueur " + player.getDisplayName() + " �ea atteint le niveau " + this.level + " !"));

				final Firework f = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
				f.detonate();
				final FireworkMeta fM = f.getFireworkMeta();
				final FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(Color.AQUA)
						.withFade(Color.GREEN).withFade(Color.FUCHSIA).with(Type.BURST).trail(true).build();
				fM.setPower(2);
				fM.addEffect(effect);
				f.setFireworkMeta(fM);
			}
		}

		if (levelChanged) {
			if (!VanishCmd.getVanished().contains(player)) {

				ParticleEffect.FLAME.display(0.1f, 0.1f, 0.1f, 0.05f, 200, player.getLocation(), 10);
				TitleManager.sendLevelMessageActionBar(player, this.level);
				player.sendMessage(
						PrefixMessage.PREFIX + "F�licitation vous avez atteint le niveau " + this.level + " !");
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
				final Firework f = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
				f.detonate();
				final FireworkMeta fM = f.getFireworkMeta();
				final FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(Color.RED)
						.withFade(Color.BLUE).with(Type.STAR).trail(true).build();
				fM.setPower(1);
				fM.addEffect(effect);
				f.setFireworkMeta(fM);

			}
			this.playerPcm.updatePlayerNameTab();
		} else {
			TitleManager.sendExperienceMessageActionBar(player, experienceAdded);
		}
	}

	/**
	 * M�thode qui permet fixer un nombre pr�cis d'experience.
	 * 
	 * @param experience
	 *            Experience fix�.
	 */
	public final void setExperience(final long experience) {
		this.experience = experience;
		TitleManager.sendExperienceMessageActionBar(this.playerPcm.getPlayer(), experience);
	}

	/**
	 * M�thode qui retire de l'experience.
	 * 
	 * @param experience
	 *            Experience retir�.
	 */
	public final void removeExperience(final long experience) {
		setExperience(this.experience - experience);
	}

	/**
	 * M�thode qui r�cup�re l'experience du joueur.
	 * 
	 * @return L'experience du joueur.
	 */
	public final long getExperience() {
		return this.experience;
	}

	/**
	 * M�thode qui met � 0 l'experience du joueur.
	 */
	public final void resetExperience() {
		setExperience(0);
	}

	/**
	 * M�thode qui met � 1 le niveau du joueur.
	 */
	public final void resetLevels() {
		setLevel(1);
	}

	/**
	 * M�thode qui ajoute des niveau au joueur.
	 * 
	 * @param level
	 *            Niveaux ajout�s.
	 */
	public final void addLevel(final int level) {
		setLevel(this.level + level);
	}

	/**
	 * M�thode qui permet de fixer le nombre de niveau actuel du joueur.
	 * 
	 * @param level
	 *            Niveaux fix�s.
	 */
	public final void setLevel(final int level) {
		this.level = level;
		TitleManager.sendLevelMessageActionBar(this.playerPcm.getPlayer(), level);
		this.playerPcm.updatePlayerNameTab();
	}

	/**
	 * M�thode qui permet de retirer un certain nombre de niveau au joueur.
	 * 
	 * @param level
	 *            Niveaux retir�s.
	 */
	public final void removeLevel(final int level) {
		setLevel(this.level - level);
	}

	/**
	 * M�thode qui permet de r�cuperer le nombre de niveaux qu'� le joueur.
	 * 
	 * @return Les niveaux.
	 */
	public final int getLevel() {
		return this.level;
	}

	/**
	 * M�thode qui permet de r�cuperer sous {@link String} le code couleur associ�
	 * au niveau ainsi que le niveau lui m�me.
	 * 
	 * @return Le code couleur avec le niveau du joueur.
	 */
	public final String getSyntaxeLevelTab() {
		return ExperienceManager.getSyntaxeLevelTab(this.level);
	}

	/**
	 * Cette m�thode permet de remettre l'experience ainsi que les niveaux sur
	 * joueur par d�faut. Le pseudo dans le tab est mit � jour.
	 */
	public final void resetExperienceStats() {
		resetExperience();
		resetLevels();
		this.playerPcm.updatePlayerNameTab();
	}

}
