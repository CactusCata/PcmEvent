package fr.cactuscata.pcmevent.command.simplecmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.SimpleCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;

/**
 * <p>
 * La classe permet d'executer la commande /gamemodeb, cette derni�re est
 * principalement pr�vu pour les organisateurs, elle permet de rendre ces
 * derniers invincible et les met dans un gamemode 'toggler' entre le
 * {@link GameMode#SURVIVAL} et le {@link GameMode#ADVENTURE}.
 * </p>
 * <p>
 * Les joueurs qui executent cette commande sont stock�s dans la liste
 * {@link GamemodebCmd#gamemodeB}, cette derni�re est r�cup�rable via la m�thode
 * {@link GamemodebCmd#getGamemodeB()}.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.0.0
 */

public final class GamemodebCmd extends SimpleCommand {

	private static final List<Player> gamemodeB = new ArrayList<>();

	/**
	 * Constructeur qui permet d'instancier la super-class.
	 */
	public GamemodebCmd() {
		super(PrefixMessage.PREFIX, SenderType.PLAYER);
	}

	@Override
	protected final void execute(final CommandSender sender, String[] args) throws CommandException {
		final Player player = (Player) sender;
		final StringBuilder build = new StringBuilder("Vous ");
		if (gamemodeB.contains(player)) {
			player.setGameMode(GameMode.ADVENTURE);
			gamemodeB.remove(player);
			build.append("serez vis� par les mini-jeux (adventure) !");
		} else {
			player.setGameMode(GameMode.SURVIVAL);
			gamemodeB.add(player);
			build.append("ne serez plus vis� par les mini-jeux (survival) !");
		}
		super.sendMessage(sender, build);
	}

	/**
	 * M�thode qui permet de r�cuperer la liste de tous les joueurs ayant utilis� la
	 * commande <code>'/gamemodeb'</code>.
	 * 
	 * @return La liste de tous les joueurs ayant execut� la commande
	 *         <code>'/gamemodeb'</code>.
	 */
	public static final List<Player> getGamemodeB() {
		return gamemodeB;
	}

	@Override
	protected final String getHelp() {
		return "Cette commande permet de switcher de gamemode, ici de survie � aventure ou inverssement pour ne �tre cibl� par les commandblocks des events. Une fois la commande �xecut� une premi�re fois, vous serez aussi invincible et vous ne perdrez de saturation. Cette commande �tre principalement faite pour les organisateurs.";
	}

}
