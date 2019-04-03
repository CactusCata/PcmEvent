package fr.cactuscata.pcmevent.command.simplecmd.spy;

import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.other.StringUtils;

/**
 * <p>
 * Cette classe permet l'execution de la commande /r, une fois cette dernière
 * utilisé avec en argument le message, elle envois au dernier joueur qui l'a
 * {@link MsgCmd /msg} le message.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.0.0
 */

public final class RCmd extends SimpleCommand {

	/**
	 * Constructeur qui permet d'instacier la super-class.
	 */
	public RCmd() {
		super(PrefixMessage.SPY, new SenderType[] { SenderType.PLAYER, SenderType.CONSOLE });
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final CommandSender senderTarget = MsgCmd.getAnswerMap().get(sender);
		CommandValidator.isNull(senderTarget, "Commencez d'abord une discussion !");

		MsgCmd.sendMessage(sender, senderTarget, "§7[§f" + sender.getName() + "§7]->[§f" + senderTarget.getName()
				+ "§7] " + StringUtils.join(args, " "));

		MsgCmd.getAnswerMap().put(senderTarget, sender);

	}

	@Override
	protected final String getHelp() {
		return "Grace à cette commande vous pourrez répondre au message du précédant joueur ayant entamé une discussion avec vous.";
	}

}
