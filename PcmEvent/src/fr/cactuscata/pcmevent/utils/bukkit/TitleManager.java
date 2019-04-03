package fr.cactuscata.pcmevent.utils.bukkit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.cactuscata.pcmevent.utils.experience.ExperienceManager;
import fr.cactuscata.pcmevent.utils.other.Reflection;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

/**
 * <p>
 * Cette classe permet d'envoyer un message en actionbar ou un title à un
 * joueur.
 * </p>
 * 
 * @author CactusCata
 * @version 2.4.1
 * @since 2.4.0
 * @see ExperienceManager
 */

public final class TitleManager {

	/**
	 * Méthode qui permet d'nvoyer un message en actionBar.
	 * 
	 * @param player
	 *            Joueur cible.
	 * @param experienceAdded
	 *            Nombre présenté.
	 */
	public static final void sendExperienceMessageActionBar(final Player player, final long experienceAdded) {
		sendActionBarMessage(player,
				"[\"\",{\"text\":\"§6§l[\"},{\"text\":\" \"},{\"text\":\"§2 "
						+ (experienceAdded > 0 ? ChatColor.GREEN + "+ " : ChatColor.RED + "- ") + experienceAdded
						+ " xp \"},{\"text\":\" \"},{\"text\":\"§6§l]\"}]");
	}

	/**
	 * Méthode qui permet d'envoyer un message en actionBar.
	 * 
	 * @param player
	 *            Joueur cible.
	 * @param amount
	 *            Nombre présenté.
	 */
	public static final void sendLevelMessageActionBar(final Player player, final int amount) {
		sendActionBarMessage(player,
				"[\"\",{\"text\":\"§6§l[\"},{\"text\":\" \"},{\"text\":\"§2 "
						+ (amount > 0 ? ChatColor.GREEN + "+ " : ChatColor.RED + "- ") + amount
						+ " levels \"},{\"text\":\" \"},{\"text\":\"§6§l]\"}]");
	}

	/**
	 * Méthode qui permet d'executer les deux méthodes
	 * {@link TitleManager#sendExperienceMessageActionBar(Player, long)} et
	 * {@link TitleManager#sendLevelMessageActionBar(Player, int)}.
	 * 
	 * @param player
	 *            Joueur cible.
	 * @param message
	 *            Message en formatJSON.
	 */
	public static final void sendActionBarMessage(final Player player, final String message) {
		Reflection.sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(message), (byte) 2), player);
	}

	/**
	 * Méthode qui permet d'envoyer un title à un joueur.
	 * 
	 * @param player
	 *            Joueur cible.
	 * @param upText
	 *            Texte supérieur.
	 * @param downText
	 *            Texte inférieur.
	 * @param fadeIn
	 *            Temps d'apparition (en ticks).
	 * @param timeTitleShow
	 *            Temps de durée (en ticks).
	 * @param fadeOut
	 *            Temps de disparition (en ticks).
	 */
	public static final void sendTitle(final Player player, final String upText, final String downText,
			final int fadeIn, final int timeTitleShow, final int fadeOut) {

		Reflection.sendPacket(
				new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\": \"" + upText + "\"}")),
				player);
		Reflection.sendPacket(
				new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a("{\"text\":\"" + downText + "\"}")),
				player);
		Reflection.sendPacket(new PacketPlayOutTitle(fadeIn, timeTitleShow, fadeOut), player);
	}

	/**
	 * Mothode qui permet de clear les titles du joueur.
	 * 
	 * @param player
	 *            Joueur cible.
	 */
	public static final void clearTitle(final Player player) {
		PacketPlayOutTitle clear = new PacketPlayOutTitle(EnumTitleAction.CLEAR, ChatSerializer.a(""));
		Reflection.sendPacket(clear, player);
	}

}
