package fr.cactuscata.pcmevent.command.nosimplecmd.experience;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SubCommand;
import fr.cactuscata.pcmevent.utils.bukkit.Npc;
import fr.cactuscata.pcmevent.utils.bukkit.PlayerPcm;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.experience.ExperienceClassementSystem;
import fr.cactuscata.pcmevent.utils.sql.Sql;

/**
 * Cette classe permet le fonctionnement de l'argument <code>'update''</code> de
 * la commande <code>'/experience'</code>. Celle-ci permet de mettre à jour les
 * données des joueurs dans la {@link Sql base de donnée} et regenerer les
 * {@link Npc}.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.4.1
 * @see {@link ExperienceCmd}, {@link ExperienceCmdExperience},
 *      {@link ExperienceCmdLevel}.
 */

final class ExperienceCmdUpdate extends SubCommand {

	/**
	 * Définis les valeurs par défaut pour sa super-classe {@link SubCommand}.
	 * 
	 * @param plugin
	 *            Permet de faire le delais avec {@link Bukkit#getScheduler()} pour
	 *            le temps de mise à jour (30 secondes).
	 */
	ExperienceCmdUpdate() {
		super("update", SenderType.ALL);
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {
		CommandValidator.isNot(sender.getName(), "CactusCata", "Seulement CactusCata peut executer la commande !");
		//CommandValidator.isNot(this.toggle, false, "Une update a été faite il y a moins de 30 secondes !");
		sender.sendMessage(PrefixMessage.PREFIX + "§6Mise à jour en cours...");
		Bukkit.getOnlinePlayers().forEach(player -> PlayerPcm.getPlayersPcm().get(player).saveInSql(true));
		//ExperienceClassementSystem.unregisterAllNPC();
		ExperienceClassementSystem.registerExpInfo();
		// ExperienceClassementSystem.spawnNPC(this.plugin);
		sender.sendMessage(PrefixMessage.PREFIX + "§2La liste des joueurs a été mise à jour !");

//		toggle = true;
//		Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable() {
//
//			@Override
//			public final void run() {
//				ExperienceCmdUpdate.this.toggle = false;
//			}
//		}, 620L);

	}

	@Override
	protected final String getHelp() {
		return "Cet argument permet de mettre à jour les npc ainsi que les statistiques d'experience des joueurs connectés.";
	}

}
