package fr.cactuscata.pcmevent.utils.bukkit;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * <p>
 * Cette classe qui fonctionne sous forme de buildPatern permet de créer
 * facilement des items.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.3.0
 */

public final class ItemBuilder {

	private ItemStack item;
	private ItemMeta itemM;

	/**
	 * @param material
	 *            Materiel.
	 * @param amount
	 *            Nombre d'items.
	 * @param damageValue
	 *            La valeur de la damageValue.
	 */
	public ItemBuilder(final Material material, final int amount, final short damageValue) {
		this.item = new ItemStack(material, amount, damageValue);
		this.itemM = this.item.getItemMeta();
	}

	/**
	 * 
	 * @param material
	 *            Materiel.
	 * @param amount
	 *            Nombre d'items.
	 */
	public ItemBuilder(final Material material, final int amount) {
		this(material, amount, (byte) 0);
	}

	/**
	 * 
	 * @param material
	 *            Materiel.
	 * @param damageValue
	 *            La valeur de la damageValue.
	 */
	public ItemBuilder(final Material material, final short damageValue) {
		this(material, 1, damageValue);
	}

	/**
	 * 
	 * @param material
	 *            Materiel.
	 */
	public ItemBuilder(final Material material) {
		this(material, 1, (byte) 0);
	}

	/**
	 * 
	 * @param item
	 *            ItemStack.
	 */
	public ItemBuilder(final ItemStack item) {
		this.item = item;
		this.itemM = item.getItemMeta();
	}

	/**
	 * Méthode qui permet de créer créer un crâne
	 * 
	 * @param playerName
	 *            Nom du joueur.
	 * @return return Le crâne du joueur.
	 */
	public static final ItemStack createSkull(final String playerName) {
		final ItemStack item = new ItemBuilder(Material.SKULL_ITEM, (byte) 3).build();
		final SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
		meta.setOwner(playerName);
		item.setItemMeta(meta);
		return item;
	}

	/**
	 * Méthode qui permet de colorer les items de {@link Material#LEATHER_BOOTS},
	 * {@link Material#LEATHER_LEGGINGS}, {@link Material#LEATHER_CHESTPLATE} et
	 * {@link Material#LEATHER_HELMET}.
	 * 
	 * @param red
	 *            Rouge.
	 * @param green
	 *            Vert.
	 * @param blue
	 *            Bleu
	 * @return L'instance de la classe.
	 */
	public final ItemBuilder color(final int red, final int green, final int blue) {
		((LeatherArmorMeta) this.itemM).setColor(Color.fromBGR(red, green, blue));
		return this;
	}

	/**
	 * Méthode qui permet de créer une description et de la placer sur l'item en
	 * question.
	 * 
	 * @param lore
	 *            Liste de {@link String} qui définissent la désctiption de l'item.
	 * @return L'instance de la classe.
	 */
	public final ItemBuilder setLore(final List<String> lore) {
		this.itemM.setLore(lore);
		return this;
	}

	/**
	 * Méthode qui permet de placer un nom customié pour l'item.
	 * 
	 * @param displayName
	 *            Nom customisé.
	 * @return L'instance de la classe.
	 */
	public final ItemBuilder setDisplayName(final String displayName) {
		this.itemM.setDisplayName(displayName);
		return this;
	}

	/**
	 * Méthode qui rend invulnérable aux dégats d'utilisation.
	 * 
	 * @return L'instance de la classe.
	 */
	public final ItemBuilder setUnbreakable() {
		this.itemM.spigot().setUnbreakable(true);
		return this;
	}

	/**
	 * Méthode qui permet d'ajouter un enchantement à l'tem.
	 * 
	 * @param enchentement
	 *            L'enchantement voulu.
	 * @param level
	 *            Le niveay d'enchantement.
	 * @return L'instance de la classe.
	 */
	public final ItemBuilder addEnchantement(final Enchantment enchentement, final int level) {
		this.itemM.addEnchant(enchentement, level, true);
		return this;
	}

	/**
	 * Méthode qui permet de cacher certains attributs de l'item comme sa durabilité
	 * ou ses enchantements.
	 * 
	 * @param itemFlags
	 *            Se qui l'énuméartion va cacher.
	 * @return L'instance de la classe.
	 */
	public final ItemBuilder addItemFlags(final ItemFlag... itemFlags) {
		this.itemM.addItemFlags(itemFlags);
		return this;
	}

	/**
	 * Méthode qui permet d'ajouter un effect visiel d'enchantement.
	 * 
	 * @return L'instance de la classe.
	 */
	public final ItemBuilder setGlowing() {
		addEnchantement(Enchantment.DAMAGE_UNDEAD, 0);
		addItemFlags(ItemFlag.HIDE_ENCHANTS);
		return this;
	}

	/**
	 * Méthode qui permet d'ajouter un effect visuel d'enchantement sous condition.
	 * 
	 * @param wantGlowing
	 *            met le glowing si <code>true</code>.
	 * @return L'instance de la classe.
	 */
	public final ItemBuilder setGlowing(final boolean wantGlowing) {
		if (wantGlowing)
			setGlowing();
		return this;
	}

	/**
	 * Méthode qui permet de récuperer l'ItemStack à partie de cette classe.
	 * 
	 * @return L'ItemStack associé au build.
	 */
	public final ItemStack build() {
		this.item.setItemMeta(this.itemM);
		return this.item;
	}

	/**
	 * Méthode qui convertie cette classe en {@link CraftItemStack}.
	 * 
	 * @return {@link CraftItemStack}.
	 */
	public final CraftItemStack toCraftItemStack() {
		return CraftItemStack.asCraftCopy(this.build());
	}

	/**
	 * Méthode qui conertie cette classe en Nms
	 * {@link net.minecraft.server.v1_8_R3.ItemStack}.
	 * 
	 * @return {@link net.minecraft.server.v1_8_R3.ItemStack}.
	 */
	public final net.minecraft.server.v1_8_R3.ItemStack toNMSItemStack() {
		return CraftItemStack.asNMSCopy(this.build());
	}

}
