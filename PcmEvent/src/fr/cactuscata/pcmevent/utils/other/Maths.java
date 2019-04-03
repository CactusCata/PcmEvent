package fr.cactuscata.pcmevent.utils.other;

/**
 * <p>
 * Cette classe permet d'arrondir des nombres avec un certains montant après la
 * virgule.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 1.0.0
 */

public final class Maths {

	/**
	 * Cette méthode permet d'arrondir une valeur de type <code>float</code> en
	 * <code>double</code>.
	 * 
	 * @param value
	 *            Nombre à arrondir.
	 * @param nbrvirgule
	 *            Valeur après la virgule.
	 * @return Le nombre de base arrondie.
	 */
	public final static double arrondidouble(final double value, final int nbrvirgule) {
		return (double) ((int) (value * Math.pow(10, nbrvirgule) + .5)) / Math.pow(10, nbrvirgule);
	}

	/**
	 * Cette méthode permet d'arrondir une valeur de type <code>float</code>.
	 * 
	 * @param value
	 *            Nombre à arrondir.
	 * @param nbrvirgule
	 *            Valeur après la virgule.
	 * @return Le nombre de base arrondie.
	 */
	public final static float arrondifloat(final float value, final int nbrvirgule) {
		return (float) arrondidouble(value, nbrvirgule);
	}

	/**
	 * Méthode qui effectue la multiplication du nombre par lui même, aussi appelé
	 * "mise au carré".
	 * 
	 * @param num
	 *            le nombre cible.
	 * @return Le nombre au carré.
	 */
	public final static double square(final double num) {
		return num * num;
	}

	public static int floor(final double num) {
		int floor = (int) num;
		return floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
	}
}
