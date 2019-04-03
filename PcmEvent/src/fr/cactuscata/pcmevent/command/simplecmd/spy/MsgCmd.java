package fr.cactuscata.pcmevent.command.simplecmd.spy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.command.simplecmd.vanish.VanishCmd;
import fr.cactuscata.pcmevent.utils.bukkit.PlayerPcm;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.other.StringUtils;

/**
 * <p>
 * Cette classe permet l'execution de la commande /msg, qui une fois executé,
 * permettra d'envoyer un message privé à joueur.
 * </p>
 * <p>
 * Il permettra de mettre à jour le {@link RCmd /r} ainsi qu'envoyer le
 * précédant message à tout ceux qui ont le {@link SocialSpyCmd} d'activé.
 * </p>
 * 
 * @author CactusCata
 * @version 2.4.1
 * @since 1.0.0
 */

public final class MsgCmd extends SimpleCommand {

	private static final Map<CommandSender, CommandSender> answerMap = new HashMap<>();

	/**
	 * Constructeur qui instancie la super classe.
	 */
	public MsgCmd() {
		super(PrefixMessage.PREFIX, SenderType.ALL, "Veuillez préciser le joueur !", "Veuillez préciser le message !");
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final Player playerTarget = CommandValidator.getPlayerByString(args[0]);
		CommandValidator.isTrue(
				!CommandValidator.isOnline(playerTarget)
						|| ((sender instanceof Player) && VanishCmd.getVanished().contains(playerTarget)
								&& !PlayerPcm.getPlayersPcm().get((Player) sender).getRank().isStaff()),
				"Le joueur '" + args[0] + "' n'est pas en ligne !");
		sendMessage(sender, playerTarget, "§7[§f" + sender.getName() + "§7]->[§f" + playerTarget.getName() + "§7] "
				+ StringUtils.join(args, 1, " "));

		getAnswerMap().put(playerTarget, sender);

	}

	public static final Map<CommandSender, CommandSender> getAnswerMap() {
		return MsgCmd.answerMap;
	}

	@Override
	protected final String getHelp() {
		return "Vous pourrez grace à cette commande, envoyer un message privé à un joueur.";
	}

	@Override
	protected final List<String> onTabComplete(final String[] args) {
		return (args.length == 1) ? PlayerPcm.getCorrectPlayersNames() : Arrays.asList("");
	}

	static final void sendMessage(final CommandSender sender, final CommandSender recever, final String message) {
		sender.sendMessage(message);
		recever.sendMessage(message);
		SocialSpyCmd.getSocial().stream().filter(playerSpy -> playerSpy != recever && playerSpy != sender)
				.forEach(playerSpy -> playerSpy.sendMessage(PrefixMessage.SPY + message));
	}

}
