package fr.cactuscata.pcmevent.command.simplecmd.inventoryviewver;

import java.util.Arrays;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import fr.cactuscata.pcmevent.PcmEvent;
import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PlayerPcm;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.custominventory.InventoryHelper;

/**
 * 
 * <b>OpenInv est une classe qui permet d'acceder à l'inventaire d'un
 * joueur.</b>
 * <p>
 * En effet, grace à cette classe, on peut récuperer le contenu de l'inventaire
 * d'un joueur, qu'il soit déconnecté ou non.
 * </p>
 * <p>
 * Les modifications du contenu seront mises à jour pour le joueur.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.1.0
 */
public final class OpenInvCmd extends SimpleCommand {

	private final InventoryHelper inventoryHelper;

	/**
	 * Garde en mémoire {@link Plugin} ainsi que {@link InventoryHelper}.
	 * 
	 * @param plugin
	 *            Pour faire de l'asychrone.
	 * @param inventoryHelper
	 *            Pour récuperer l'inventaire du joueur ciblé.
	 */
	public OpenInvCmd(final InventoryHelper inventoryHelper) {
		super(PrefixMessage.PREFIX, SenderType.PLAYER, "Veuillez préciser le joueur");
		this.inventoryHelper = inventoryHelper;
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		final Player playerSender = (Player) sender;

		CommandValidator.is(playerSender.getName(), args[0], "Vous ne pouvez pas ouvrir votre propre inventaire !");
		final Plugin plugin = PcmEvent.getPlugin();

		new BukkitRunnable() {

			@Override
			public final void run() {
				final OfflinePlayer offlinePlayer = InventoryHelper.matchPlayer(plugin, args[0]);
				if ((offlinePlayer == null) || ((!offlinePlayer.hasPlayedBefore()) && (!offlinePlayer.isOnline()))) {
					playerSender.sendMessage(PrefixMessage.PREFIX + "Le joueur " + args[0] + " n'existe pas !");
					return;
				}
				new BukkitRunnable() {
					@Override
					public void run() {
						if (!playerSender.isOnline())
							return;

						final boolean online = offlinePlayer.isOnline();
						final Player onlineTarget;
						if (!online) {
							onlineTarget = InventoryHelper.loadPlayer(offlinePlayer);
							if (onlineTarget == null)
								playerSender.sendMessage(PrefixMessage.PREFIX + "Le joueur n'existe pas !");

						} else
							onlineTarget = offlinePlayer.getPlayer();

						playerSender.openInventory(OpenInvCmd.this.inventoryHelper.getInventory(onlineTarget, online)
								.getBukkitInventory());
					}
				}.runTask(plugin);
			}
		}.runTaskAsynchronously(plugin);
	}

	@Override
	protected final String getHelp() {
		return "Cette commane permet d'acceder à l'inventaire du joueur précisé.";
	}

	@Override
	protected final List<String> onTabComplete(final String[] args) {
		return args.length == 1 ? PlayerPcm.getCorrectPlayersNames() : Arrays.asList("");
	}

}
