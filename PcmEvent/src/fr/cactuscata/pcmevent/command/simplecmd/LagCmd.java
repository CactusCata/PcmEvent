package fr.cactuscata.pcmevent.command.simplecmd;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Player;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.command.simplecmd.vanish.VanishCmd;
import fr.cactuscata.pcmevent.utils.bukkit.PlayerPcm;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.bukkit.Tps;
import fr.cactuscata.pcmevent.utils.other.Maths;

/**
 * <p>
 * Cette classe permet d'executer la commande /lag, cette dernière permet de
 * récuperer son {@link PlayerPcm#getPing() ping} ainsi que les
 * {@link Tps#getTps() tps} moyens du serveur.
 * </p>
 * <p>
 * Si le nom d'un joueur est mis en argument, son {@link PlayerPcm#getPing()
 * ping} sera alors affiché pour le joueur qui a écrit la commande.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.5.0
 */

public final class LagCmd extends SimpleCommand {

	/**
	 * Constructeur qui permet d'instancier la super-class.
	 */
	public LagCmd() {
		super(PrefixMessage.PREFIX, SenderType.ALL);
	}

	@Override
	public final void execute(final CommandSender sender, final String[] args) throws CommandException {
		System.out.println(Arrays.asList(((CraftServer) Bukkit.getServer()).getHandle().getServer().recentTps));
		if (args.length == 0) {
			final StringBuilder message = new StringBuilder("§3Tps serveur : §e")
					.append(Maths.arrondidouble(Tps.getTps(), 3)).append("\n").append(PrefixMessage.PREFIX)
					.append("§3Tps serveur en % : §e").append(Math.round((1.0D - Tps.getTps() / 20.0D) * 100.0D))
					.append(" %");

			if (sender instanceof Player)
				message.append("\n").append(PrefixMessage.PREFIX).append("§3Latence avec le server : §e")
						.append(PlayerPcm.getPlayersPcm().get((Player) sender).getPing()).append("§3 ms !");

			super.sendMessage(sender, message);
		} else {

			final Player target = CommandValidator.getPlayerByString(args[0]);
			CommandValidator.isTrue(
					!CommandValidator.isOnline(target)
							|| ((sender instanceof Player) && VanishCmd.getVanished().contains(target)
									&& PlayerPcm.getPlayersPcm().get((Player) sender).getRank().isStaff()),
					"Le joueur '" + args[0] + "' n'est pas en ligne !");
			super.sendMessage(sender, "Latence du joueur " + target.getDisplayName() + "§3 : "
					+ PlayerPcm.getPlayersPcm().get(target).getPing() + "§3 ms !");
		}
	}

	@Override
	protected final String getHelp() {
		return "Cette commande permet de récuperer les tps du serveur ainsi que les ms du joueur qui a éxectué la commande, il permet aussi de récuperer les ms d'un joueur préciser.";
	}

	@Override
	protected final List<String> onTabComplete(final String[] args) {
		return (args.length == 1) ? PlayerPcm.getCorrectPlayersNames() : Arrays.asList("");
	}

}
