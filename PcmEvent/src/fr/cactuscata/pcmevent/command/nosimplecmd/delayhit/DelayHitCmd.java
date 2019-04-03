package fr.cactuscata.pcmevent.command.nosimplecmd.delayhit;

import fr.cactuscata.pcmevent.command.NotSimpleCommand;

/**
 * Classe qui permet d'executer la commande /hitdelay, cette derni�re permet de
 * mettre un delais (en tick) sur un cible, celle-ci ne pourra recevoir un coup
 * qu'apr�s le nombre indiqu� (toujours en tick). La classe met a disposition
 * quatres arguments qui sont [add/set/remove/reset].
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.2.0
 * @see DelayHitCmdAdd DelayHitCmdRemove DelayHitCmdReset DelayHitCmdSet
 */

public final class DelayHitCmd extends NotSimpleCommand {

	/**
	 * Instancie les objets {@link DelayHitCmdAdd}, {@link DelayHitCmdRemove},
	 * {@link DelayHitCmdReset} et {@link DelayHitCmdSet}.
	 */
	public DelayHitCmd() {
		super(new DelayHitCmdAdd(), new DelayHitCmdRemove(), new DelayHitCmdSet(), new DelayHitCmdReset());
	}

	@Override
	protected final String getTutorialCommand() {
		return "Grace � cette commande vous pourrez modifier le delais entre lequel les gens ne pourront plus me de d�gats au joueur pr�ciser. Fonctionne avec les sous-arguments [add/set/remove/reset].";
	}

}
