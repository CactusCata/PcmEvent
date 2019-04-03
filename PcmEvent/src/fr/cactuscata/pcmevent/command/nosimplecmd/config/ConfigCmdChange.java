package fr.cactuscata.pcmevent.command.nosimplecmd.config;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import fr.cactuscata.pcmevent.command.CCommandExecutor.SenderType;
import fr.cactuscata.pcmevent.command.simplecmd.vanish.VanishFile;
import fr.cactuscata.pcmevent.command.CommandException;
import fr.cactuscata.pcmevent.command.CommandValidator;
import fr.cactuscata.pcmevent.command.SubCommand;
import fr.cactuscata.pcmevent.utils.bukkit.PrefixMessage;
import fr.cactuscata.pcmevent.utils.other.StringUtils;

/**
 * <p>
 * Cette classe instancié par l'objet {@link ConfigCmd} est un des arguments de
 * la commande <code>/config</code> et est représenté par <code>'change'</code>.
 * Elle permet de modifier les valeurs de l'objet {@link VanishFile}.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.5.0
 * @see {@link VanishFile}, {@link ConfigCmd}, {@link ConfigCmdList}.
 */

final class ConfigCmdChange extends SubCommand {

	private final VanishFile vanishFile;

	/**
	 * 
	 * @param vanishFile
	 *            Permet d'utiliser la méthode
	 *            {@link VanishFile#changeValue(String, boolean)} pour modifier la
	 *            valeur voulu.
	 */
	ConfigCmdChange(final VanishFile vanishFile) {
		super("change", SenderType.ALL, "Veuillez préciser un argument valide (essayez /config list)");
		this.vanishFile = vanishFile;
	}

	@Override
	protected final void execute(final CommandSender sender, final String[] args) throws CommandException {

		final String value = StringUtils.getCorrectStringInArray(this.vanishFile.getAllEntryMap(), args[0]);
		CommandValidator.isNull(value, "La section '" + args[0] + "' n'est pas une section correcte !");
		this.vanishFile.toogleValue(value);
		sender.sendMessage(PrefixMessage.CONFIG + "La valeur de Vanish." + args[0] + " a été mise à "
				+ !this.vanishFile.getValue(value) + " !");

	}

	@Override
	protected final String getHelp() {
		return "Vous pouvez, grace à cet argument vous pouvez modifier la valeur des différents paths !";
	}

	@Override
	protected final List<String> getSubArguments() {
		return Arrays.asList(this.vanishFile.getAllEntryMap());
	}

}
