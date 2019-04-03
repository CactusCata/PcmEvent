package fr.cactuscata.pcmevent.command.nosimplecmd.holo;

import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.SubCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.hologram.HoloFile;
import fr.cactuscata.pcmevent.utils.hologram.objects.NamedHologramManager;

/**
 * Cette classe permet l'utilisation de l'argument <code>'reload'</code> de la
 * commande <code>'/holo'</code>.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see {@link HoloCmd}
 */

final class HoloCmdReload extends SubCommand {

	private final HoloFile holoFile;

	/**
	 * Définis les valeurs par défaut pour sa super-classe {@link SubCommand}.
	 * 
	 * @param holoFile
	 *            Permet de remettre tous les hologrammes à jour.
	 */
	HoloCmdReload(final HoloFile holoFile) {
		super("reload", SenderType.ALL);
		this.holoFile = holoFile;
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {

		final long time = System.currentTimeMillis();
		NamedHologramManager.clearAll();
		this.holoFile.reload();
		NamedHologramManager.getHolograms().forEach(holo -> holo.refreshAll());

		sender.sendMessage(PrefixMessage.PREFIX + "Reload effectué en " + (System.currentTimeMillis() - time) + " ms !");
	}

	@Override
	protected final String getHelp() {
		return "Grace à cet argument vous pourrez mettre à jour tous les hologrammes.";
	}

}
