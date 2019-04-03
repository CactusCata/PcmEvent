package fr.cactuscata.pcmevent.command.nosimplecmd.physic;

import fr.cactuscata.pcmevent.command.NotSimpleCommand;

/**
 * Cette classe permet de gerer la commande <code>'/physic'</code> qui permet
 * momentanément de désactiver la physic grade à l'argument
 * {@link PhysicCmdDisable 'disable'}, vous pouvez également filtrer les blocs
 * voulu grace à l'argument {@link PhysicCmdAdd 'add'} et les retirer avec
 * {@link PhysicCmdRemove 'remove'}. Vous pouvez voir la liste de tous les
 * matérieux withlist avec l'argument {@link PhysicCmdList 'list'}. Pour
 * réactiver la physique, vous n'avez qu'à utiliser l'argument
 * {@link PhysicCmdEnable 'enable'}.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.5.1
 * @see PhysicCmdAdd PhysicCmdDisable PhysicCmdEnable PhysicCmdList
 *      PhysicCmdRemove
 *
 */

public final class PhysicCmd extends NotSimpleCommand {

	/**
	 * Instanciation de tous les arguments.
	 * 
	 * @param physicFile
	 *            Argument requis pour l'instanciation des différents
	 *            objet-arguments.
	 */
	public PhysicCmd(final PhysicFile physicFile) {
		super(new PhysicCmdList(physicFile), new PhysicCmdAdd(physicFile), new PhysicCmdRemove(physicFile),
				new PhysicCmdEnable(physicFile), new PhysicCmdDisable(physicFile));
	}

	@Override
	protected final String getTutorialCommand() {
		return "Grace à cette commande vous pourrez ajouter ou supprimer des materials qui subiront la physique. (liste de tous les materials: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html ).";
	}

}
