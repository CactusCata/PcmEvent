package fr.cactuscata.pcmevent.utils.bukkit;

import org.bukkit.entity.Player;

import fr.cactuscata.pcmevent.listener.JoinGameListener;
import fr.cactuscata.pcmevent.utils.other.Reflection;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;

/**
 * <p>
 * Cette classe, utilisé pour l'instant seulement dans la classe
 * {@link JoinGameListener},permet de mettre un tablist avec un description via
 * packet ({@link PacketPlayOutPlayerListHeaderFooter}).
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.5.0
 * @see JoinGameListener
 */

public final class SetTab {

	/**
	 * Méthode executé pour ajouter un header et un footer dans le tab à la
	 * connection d'un joueur.
	 * 
	 * @param player
	 *            Joueur qui vera son tab modifié.
	 */
	public static final void sendtab(final Player player) {

		final PacketPlayOutPlayerListHeaderFooter headerfooter = new PacketPlayOutPlayerListHeaderFooter();

		Reflection.setValue(headerfooter, "a", ChatSerializer.a(
				"[\"\",{\"text\":\"§r     §c§m----------------§8§m§l[-§r §7§k:§r §6§lPleaseCraftMe§r §7§k:§8§m§l-]§c§m----------------\n§eMonde Event\n\"}]"));
		Reflection.setValue(headerfooter, "b", ChatSerializer.a(
				"[\"\",{\"text\":\"\n§aSite & forum: §ehttp://pleasecraftme.fr\n§bIP TeamSpeak: §ets3.pleasecraftme.fr\n§dVote §d: §ehttp://vote.pleasecraftme.fr\n\"}]"));

		Reflection.sendPacket(headerfooter, player);

	}
}