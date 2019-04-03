package fr.cactuscata.pcmevent.command.nosimplecmd.holo;

import fr.cactuscata.pcmevent.command.NotSimpleCommand;
import fr.cactuscata.pcmevent.utils.hologram.HoloFile;

/**
 * Cette classe permet l'instanciation de tous les objets propres aux arguments
 * de la comma,de <code>'/holo'</code>.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.0
 * @see fr.cactuscata.pcmevent.command.nosimplecmd.holo
 */

public final class HoloCmd extends NotSimpleCommand {

	public HoloCmd(final HoloFile holoFile) {
		super(new HoloCmdAddLine(holoFile), new HoloCmdCopy(holoFile), new HoloCmdCreate(holoFile), new HoloCmdDelete(),
				new HoloCmdInfo(holoFile), new HoloCmdInsertLine(holoFile), new HoloCmdList(), new HoloCmdMoveHere(),
				new HoloCmdReload(holoFile), new HoloCmdRemoveLine(), new HoloCmdSetLine(holoFile),
				new HoloCmdTeleport());
	}

	@Override
	protected final String getTutorialCommand() {
		return "Grace à cette commande vous pourrez modifier, créer ou même supprimer des hologrammes. Vous pourrez tout aussi les supprimer, se téléporter à eux ainsi que les remettre à jour.";
	}

}
