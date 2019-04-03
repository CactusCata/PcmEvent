package fr.cactuscata.pcmevent.command.simplecmd.rank;

/**
 * <p>
 * Cette classe énumère tous les grades existants sur le monde event.
 * </p>
 * <p>
 * La mise en place d'un de ces grades se fait via la commande {@link SetRankCmd
 * /setstaff}.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.3
 * @since 1.0.0
 * @see SetRankCmd
 */

public enum RankList {

	FONDATEUR("§c§l[Fondateur]", "§b", "Fondateur", Group.STAFF, "administrateur"),
	ADMINISTRATEUR("§c[Admin]", FONDATEUR.suffix, "Administrateur", Group.STAFF, "administrateur"),
	DEVELOPPEUR("§2[Dev]", FONDATEUR.suffix, "Developpeur", Group.STAFF, "administrateur"),
	RESPONSABLE_EVENTS("§5[Resp.Events]", "§3", "Resp.Events", Group.STAFF, "responsable"),
	RESPONSABLE_RECRUTEMENT("§5[Resp.Recru]", RESPONSABLE_EVENTS.suffix, "Resp.Recrutements", Group.STAFF, "responsable"),
	RESPONSABLE_BUILDER("§5[Resp.Builder]", RESPONSABLE_EVENTS.suffix, "Resp.Builders", Group.STAFF, "responsable"),
	ANIMATEUR("§9[Animateur]", "§b", "Animateur", Group.STAFF, "animateur"),
	ANIMATRICE("§9[Animatrice]", ANIMATEUR.suffix, "Animatrice", Group.STAFF, "animateur"),
	ORGANISATEUR("§9[Organisateur]", "§d", "Organisateur", Group.STAFF, "organisateur"),
	ORGANISATRICE("§9[Organisatrice]", ORGANISATEUR.suffix, "Organisatrice", Group.STAFF, "organisateur"),
	COMMUNITY_MANAGER("§1[Commu.M]", ORGANISATEUR.suffix, "CommunityM", Group.NOSTAFF, "aucun"),
	MODERATEUR("§d[Modo]", "§3", "Moderateur", Group.NOSTAFF, "aucun"),
	MODERATEUR_FORUM("§d[Modo.Forum]", MODERATEUR.suffix, "ModerateurForum", Group.NOSTAFF, "aucun"),
	GUARDIAN("§a[Guardian]", MODERATEUR.suffix, "Guardian", Group.NOSTAFF, "aucun"),
	GUIDE("§7[Guide]", MODERATEUR.suffix, "Guide", Group.PLAYER, "aucun"),
	YOUTUBEUR("§f[You§4Tube]", MODERATEUR.suffix, "Youtubeur", Group.PLAYER, "aucun"),
	ANIMATEUR_TS("§7[Animateur§fTS§7]", MODERATEUR.suffix, "AnimateurTS", Group.PLAYER, "aucun"),
	ANIMATRICE_TS("§7[Animatrice§fTS§7]", MODERATEUR.suffix, "AnimatriceTS", Group.PLAYER, "aucun"),
	ANCIEN("§8[Ancien]", MODERATEUR.suffix, "Ancien", Group.PLAYER, "aucun"),
	ANCIENNE("§8[Ancienne]", MODERATEUR.suffix, "Ancienne", Group.PLAYER, "aucun"),
	PCT("§2[PCT]", MODERATEUR.suffix, "Pct", Group.PLAYER, "aucun"),
	AMI("§6[Ami]", MODERATEUR.suffix, "Ami", Group.PLAYER, "aucun"),
	PROPLAYERPVP2K17SOUPNOPOT("§6[§2P§4P§bPvP§d2k17§9Soup§eNoPot§6]§f", "§6", "PPPvP2k17SoupNoPot", Group.PLAYER, "aucun"),
	AUCUN("§e", "§f", "Aucun", Group.PLAYER, "aucun"),
	NULL("[APPELE]", "[CACTUSCATA]", "null", Group.PLAYER, "aucun");

	private final String prefix, suffix, nameOfStaff, nameOfPermissionGroup;
	private final boolean isStaff, acceptXP;

	/**
	 * Constructeur prennant en paramètre le prefix, suffix, nom du rank, si le
	 * joueur est staff et si il accepte de l'experience.
	 * 
	 * @param prefix
	 *            Prefix.
	 * @param suffix
	 *            Suffix.
	 * @param nameOfStaff
	 *            nom d'appellation.
	 * @param isStaff
	 *            Est staff.
	 * @param acceptXP
	 *            Accepte de l'experience.
	 */
	private RankList(final String prefix, final String suffix, final String nameOfStaff, final Group group, final String nameOfPermissionGroup) {
		this.prefix = prefix;
		this.suffix = suffix;
		this.nameOfStaff = nameOfStaff;
		this.isStaff = group.isStaff;
		this.acceptXP = group.acceptXp;
		this.nameOfPermissionGroup = nameOfPermissionGroup;
	}

	/**
	 * Méthoede qui permet de récuperer sous forme de {@link String} le prefix d'un
	 * grade.
	 * 
	 * @return Le prefix.
	 */
	public final String getPrefix() {
		return this.prefix;
	}

	/**
	 * Méthoede qui permet de récuperer sous forme de {@link String} le suffix.
	 * 
	 * @return Le suffix.
	 */
	public final String getSuffix() {
		return this.suffix;
	}

	/**
	 * Méthode qui permet de récuperer sous forme de {@link String} le nom
	 * d'appellation du rank.
	 * 
	 * @return Le nom du rank.
	 */
	public final String getNameOfRank() {
		return this.nameOfStaff;
	}

	/**
	 * Méthode qui permet de savoir si il fait bien partie du staff.
	 * 
	 * @return <code>true</code> si il est staff.
	 */
	public final boolean isStaff() {
		return this.isStaff;
	}

	/**
	 * Méthode qui permet de savoir si le rank accepte l'experience.
	 * 
	 * @return <code>true</code> si le rank accepte l'experience.
	 */
	public final boolean isAcceptXP() {
		return this.acceptXP;
	}

	/**
	 * Méthode qui récupere le rank grace à son nom.
	 * 
	 * @param nameOfRank
	 *            Le suposé nom de rank.
	 * @return Le rank.
	 */
	public final static RankList getRank(final String nameOfRank) {
		for (final RankList staff : RankList.values())
			if (staff.getNameOfRank().equals(nameOfRank))
				return staff;
		return RankList.NULL;
	}

	public String getNameOfPermissionGroup() {
		return nameOfPermissionGroup;
	}

	private static enum Group {

		STAFF(true, false),
		NOSTAFF(true, true),
		PLAYER(false, true);

		private final boolean isStaff, acceptXp;

		private Group(final boolean isStaff, final boolean acceptXp) {
			this.isStaff = isStaff;
			this.acceptXp = acceptXp;
		}

	}

}
