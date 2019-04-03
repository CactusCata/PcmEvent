package fr.cactuscata.pcmevent.utils.custominventory;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.cactuscata.pcmevent.command.simplecmd.inventoryviewver.OpenInvCmd;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.PlayerInventory;

/**
 * <p>
 * Cette classe est utilisé pour faire fonctionner la commande /openinv
 * ({@link OpenInvCmd}).
 * </p>
 * 
 * @author CactusCata
 * @version 2.4.1
 * @since 2.0.0
 * @see OpenInvCmd
 */

public final class CustomPlayerInventory extends PlayerInventory {

	private final ItemStack[] extra = new ItemStack[5];
	private final CraftInventory inventory = new CraftInventory(this);
	private boolean playerOnline = false;

	public CustomPlayerInventory(final Player bukkitPlayer, final Boolean online) {
		super(PlayerDataManager.getHandle(bukkitPlayer));
		this.playerOnline = online;
		this.items = player.inventory.items;
		this.armor = player.inventory.armor;
	}

	public final Inventory getBukkitInventory() {
		return inventory;
	}

	public final void setPlayerOnline(Player player) {
		if (!playerOnline) {
			this.player = PlayerDataManager.getHandle(player);
			this.player.inventory.items = this.items;
			this.player.inventory.armor = this.armor;
			playerOnline = true;
		}
	}

	public final void setPlayerOffline() {
		playerOnline = false;
	}

	@Override
	public final ItemStack[] getContents() {
		ItemStack[] contents = new ItemStack[getSize()];
		System.arraycopy(items, 0, contents, 0, items.length);
		System.arraycopy(armor, 0, contents, items.length, armor.length);
		return contents;
	}

	@Override
	public final int getSize() {
		return super.getSize() + 5;
	}

	@Override
	public final ItemStack getItem(int i) {
		ItemStack[] is = this.items;

		if (i >= is.length) {
			i -= is.length;
			is = this.armor;
		} else {
			i = getReversedItemSlotNum(i);
		}

		if (i >= is.length) {
			i -= is.length;
			is = this.extra;
		} else if (is == this.armor) {
			i = getReversedArmorSlotNum(i);
		}

		return is[i];
	}

	@Override
	public final ItemStack splitStack(int i, int j) {
		ItemStack[] is = this.items;

		if (i >= is.length) {
			i -= is.length;
			is = this.armor;
		} else {
			i = getReversedItemSlotNum(i);
		}

		if (i >= is.length) {
			i -= is.length;
			is = this.extra;
		} else if (is == this.armor) {
			i = getReversedArmorSlotNum(i);
		}

		if (is[i] != null) {
			ItemStack itemstack;

			if (is[i].count <= j) {
				itemstack = is[i];
				is[i] = null;
				return itemstack;
			} else {
				itemstack = is[i].cloneAndSubtract(j);
				if (is[i].count == 0) {
					is[i] = null;
				}

				return itemstack;
			}
		}

		return null;
	}

	@Override
	public final ItemStack splitWithoutUpdate(int i) {
		ItemStack[] is = this.items;

		if (i >= is.length) {
			i -= is.length;
			is = this.armor;
		} else {
			i = getReversedItemSlotNum(i);
		}

		if (i >= is.length) {
			i -= is.length;
			is = this.extra;
		} else if (is == this.armor) {
			i = getReversedArmorSlotNum(i);
		}

		if (is[i] != null) {
			ItemStack itemstack = is[i];

			is[i] = null;
			return itemstack;
		}

		return null;
	}

	@Override
	public final void setItem(int i, ItemStack itemstack) {
		ItemStack[] is = this.items;

		if (i >= is.length) {
			i -= is.length;
			is = this.armor;
		} else {
			i = getReversedItemSlotNum(i);
		}

		if (i >= is.length) {
			i -= is.length;
			is = this.extra;
		} else if (is == this.armor) {
			i = getReversedArmorSlotNum(i);
		}

		// Effects
		if (is == this.extra) {
			player.drop(itemstack, true);
			itemstack = null;
		}

		is[i] = itemstack;

		player.defaultContainer.b();
	}

	private final int getReversedItemSlotNum(int i) {
		if (i >= 27) {
			return i - 27;
		}
		return i + 9;
	}

	private final int getReversedArmorSlotNum(int i) {
		if (i == 0) {
			return 3;
		}
		if (i == 1) {
			return 2;
		}
		if (i == 2) {
			return 1;
		}
		if (i == 3) {
			return 0;
		}
		return i;
	}

	@Override
	public final String getName() {
		if (player.getName().length() > 16)
			return player.getName().substring(0, 16);

		return player.getName();
	}

}