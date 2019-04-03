package fr.cactuscata.pcmevent.utils.custominventory;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.cactuscata.pcmevent.command.simplecmd.inventoryviewver.OpenEnderCmd;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IInventory;
import net.minecraft.server.v1_8_R3.InventoryEnderChest;
import net.minecraft.server.v1_8_R3.InventorySubcontainer;

/**
 * <p>
 * Cette classe est utilisé pour la gestions des enderchests, fait pour la
 * commande /openender ({@link OpenEnderCmd}).
 * 
 * @author CactusCata
 * @version 2.4.1
 * @since 2.0.0
 * @see OpenEnderCmd
 */

public final class CustomEnderChest extends InventorySubcontainer implements IInventory {

	private final InventoryEnderChest enderChest;
	private final CraftInventory inventory = new CraftInventory(this);
	private boolean playerOnline = false;

	public CustomEnderChest(final Player player, final boolean online) {
		super(PlayerDataManager.getHandle(player).getEnderChest().getName(),
				PlayerDataManager.getHandle(player).getEnderChest().hasCustomName(),
				PlayerDataManager.getHandle(player).getEnderChest().getSize());
		final EntityPlayer nmsPlayer = PlayerDataManager.getHandle(player);
		this.enderChest = nmsPlayer.getEnderChest();
		this.bukkitOwner = nmsPlayer.getBukkitEntity();
		this.items = enderChest.getContents();
	}

	public final Inventory getBukkitInventory() {
		return inventory;
	}

	public final void setPlayerOnline(final Player player) {
		if (!playerOnline) {

			try {
				EntityPlayer nmsPlayer = PlayerDataManager.getHandle(player);
				this.bukkitOwner = nmsPlayer.getBukkitEntity();
				InventoryEnderChest playerEnderChest = nmsPlayer.getEnderChest();
				Field field = playerEnderChest.getClass().getField("items");
				field.setAccessible(true);
				field.set(playerEnderChest, this.items);
			} catch (Exception e) {
			}

			playerOnline = true;
		}
	}

	public final void setPlayerOffline() {
		playerOnline = false;
	}

	@Override
	public final void update() {
		super.update();
		enderChest.update();
	}

}