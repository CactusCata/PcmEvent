package fr.cactuscata.pcmevent.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.cactuscata.pcmevent.command.simplecmd.vanish.VanishCmd;
import fr.cactuscata.pcmevent.command.simplecmd.vanish.VanishFile;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;

/**
 * <p>
 * Cette classe permet de gerer l'evenement {@link AsyncPlayerChatEvent} et
 * permet d'utiliser la m�thode {@link AsyncPlayerChatEvent#setFormat(String)}
 * pour modifier la structure du message. Il est �galement utilis� pour
 * permettre � ceux qui ont la permission {@code pcm.msg.color} d'�crire en
 * couleur avec le caract�re {@code '&'} (transform� en {@code '�'}).
 * </p>
 * <p>
 * Il y a aussi la v�rification pour ceux qui sont en {@link VanishCmd}; si
 * l'objet {@link VanishFile} a pour valeur {@code false} avec la clef
 * {@code "Vanish.CanSpeak"}, le joueur ne pourra pas envoyer de message dans le
 * chat.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.0.0
 * @see VanishFile
 */

public final class PlayerSendMessageListener implements Listener {

	private final VanishFile vanishFile;

	/**
	 * Constructeur de la classe �couteur de l'evenement
	 * {@link AsyncPlayerChatEvent}.
	 * 
	 * @param vanishFile
	 *            Pour tester si la personne est en Vanish et l'empecher de parler
	 *            si la configuration ne lui permet pas.
	 */
	public PlayerSendMessageListener(final VanishFile vanishFile) {
		this.vanishFile = vanishFile;
	}

	/**
	 * M�thode �couteur de l'evenement {@link AsyncPlayerChatEvent} qui permettra de
	 * disposer le message avec des codes couleurs <code>'�'</code> � la place de
	 * <code>'&amp;'</code>. Permet �galement de tester si la personne est en Vanish
	 * et l'empecher de parler si la configuration ne lui permet pas.
	 * 
	 * @param event
	 *            Event {@link AsyncPlayerChatEvent}.
	 */
	@EventHandler
	public final void sendMessage(final AsyncPlayerChatEvent event) {

		final Player player = event.getPlayer();

		if (VanishCmd.getVanished().contains(player) && !vanishFile.getValue("CanSpeak")) {
			player.sendMessage(PrefixMessage.VANISH
					+ "La config vous empeche de parler, vous pouvez executer la commande suivante pour parler � nouveau :\n/config change Vanish.CanSpeak");
			event.setCancelled(true);
		}

		event.setFormat("%1$s: %2$s");

	}

}
