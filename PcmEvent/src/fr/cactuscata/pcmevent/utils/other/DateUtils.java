package fr.cactuscata.pcmevent.utils.other;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Cette classe permet de récuperer la date actuel sous forme de {@link String}.
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.3.0
 *
 */

public final class DateUtils {

	/**
	 * Méthode qui récupere la date sous forme de {@link String}.
	 * 
	 * @return La date.
	 */
	public final static String getActualDate() {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
	}

}
