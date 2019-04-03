package fr.cactuscata.pcmevent.utils.hologram.nms;

import java.util.Collection;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public final class CraftNMSArmorStand extends CraftArmorStand {
	public CraftNMSArmorStand(final CraftServer server, final EntityNMSArmorStand entity) {
		super(server, entity);
	}

	@Override
	public final void remove() {
	}

	@Override
	public final void setArms(final boolean arms) {
	}

	@Override
	public final void setBasePlate(final boolean basePlate) {
	}
	
	@Override
	public final void setBodyPose(final EulerAngle pose) {
	}

	@Override
	public final void setBoots(final ItemStack item) {
	}

	@Override
	public final void setChestplate(final ItemStack item) {
	}

	@Override
	public final void setGravity(final boolean gravity) {
	}

	@Override
	public final void setHeadPose(final EulerAngle pose) {
	}

	@Override
	public final void setHelmet(final ItemStack item) {
	}

	@Override
	public final void setItemInHand(final ItemStack item) {
	}

	@Override
	public final void setLeftArmPose(final EulerAngle pose) {
	}

	@Override
	public final void setLeftLegPose(final EulerAngle pose) {
	}

	@Override
	public final void setLeggings(final ItemStack item) {
	}

	@Override
	public final void setRightArmPose(final EulerAngle pose) {
	}

	@Override
	public final void setRightLegPose(final EulerAngle pose) {
	}

	@Override
	public final void setSmall(final boolean small) {
	}

	@Override
	public final void setVisible(final boolean visible) {
	}

	@Override
	public final boolean addPotionEffect(final PotionEffect effect) {
		return false;
	}

	@Override
	public final boolean addPotionEffect(final PotionEffect effect, final boolean param) {
		return false;
	}

	@Override
	public final boolean addPotionEffects(final Collection<PotionEffect> effects) {
		return false;
	}

	@Override
	public final void setRemoveWhenFarAway(final boolean remove) {
	}

	@Override
	public final void setVelocity(final Vector vel) {
	}

	@Override
	public final boolean teleport(final Location loc) {
		return false;
	}

	@Override
	public final boolean teleport(final Entity entity) {
		return false;
	}

	@Override
	public final boolean teleport(final Location loc, final PlayerTeleportEvent.TeleportCause cause) {
		return false;
	}

	@Override
	public final boolean teleport(final Entity entity, final PlayerTeleportEvent.TeleportCause cause) {
		return false;
	}

	@Override
	public final void setFireTicks(final int ticks) {
	}

	@Override
	public final boolean setPassenger(final Entity entity) {
		return false;
	}

	@Override
	public final boolean eject() {
		return false;
	}

	@Override
	public final boolean leaveVehicle() {
		return false;
	}

	@Override
	public final void playEffect(final EntityEffect effect) {
	}

	@Override
	public final void setCustomName(final String name) {
	}

	@Override
	public final void setCustomNameVisible(final boolean flag) {
	}
}