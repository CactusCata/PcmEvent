package fr.cactuscata.pcmevent.command.simplecmd;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PlayerPcm;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.other.StringUtils;

/**
 * <p>
 * Cette classe permet d'executer la commande /killb, cette derni�re est
 * principalement utilis� par les organisateurs pour tuer quelqu'un qui ne
 * respecterait pas certaines r�gles d'events.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.0.0
 */

public final class KillbCmd extends SimpleCommand {

	/**
	 * Constructeur qui permet d'instancier la super-class.
	 */
	public KillbCmd() {
		super(PrefixMessage.PREFIX, SenderType.ALL, "Veuillez pr�ciser le joueur !", "Veuillez pr�ciser la raison !");
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final Player player = CommandValidator.getPlayerByString(args[0]);
		CommandValidator.isNotOnline(player, args[0]);
		final String message = PrefixMessage.PREFIX + "Le joueur " + player.getDisplayName()
				+ "�e a �t� tu� pour la raison �3\"" + StringUtils.join(args, 1, " ") + "\"�e !";
		player.setHealth(0.0d);
		Bukkit.getOnlinePlayers().forEach(onlinePlayers -> onlinePlayers.sendMessage(message));
		Bukkit.getLogger().log(Level.INFO, message);
	}

	@Override
	protected final String getHelp() {
		return "Cette commande permet de tuer quelqu'un en pr�cisant une raison, cette derni�re sera envoy� � tous les joueurs, cette commande est pr�vu principalement pour les organisateurs.";
	}

	@Override
	protected final List<String> onTabComplete(final String[] args) {
		return args.length == 1 ? PlayerPcm.getCorrectPlayersNames() : Arrays.asList("");
	}

}