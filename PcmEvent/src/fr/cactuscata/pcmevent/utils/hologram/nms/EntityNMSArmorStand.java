package fr.cactuscata.pcmevent.utils.hologram.nms;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;

import fr.cactuscata.pcmevent.utils.hologram.objects.lines.CraftHologramLine;
import fr.cactuscata.pcmevent.utils.other.Maths;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.Vec3D;
import net.minecraft.server.v1_8_R3.World;

public final class EntityNMSArmorStand extends EntityArmorStand {
	private boolean lockTick;
	private final CraftHologramLine parentPiece;

	public EntityNMSArmorStand(final World world, final CraftHologramLine parentPiece) {
		super(world);
		super.setInvisible(true);
		super.setSmall(true);
		super.setArms(false);
		super.setGravity(true);
		super.setBasePlate(true);
		super.n(true);
		this.parentPiece = parentPiece;
		try {
			Field field = EntityArmorStand.class.getDeclaredField("bi");
			field.setAccessible(true);
			field.set(this, Integer.valueOf(2147483647));
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public final void b(final NBTTagCompound nbttagcompound) {
	}

	@Override
	public final boolean c(final NBTTagCompound nbttagcompound) {
		return false;
	}

	@Override
	public final boolean d(final NBTTagCompound nbttagcompound) {
		return false;
	}

	@Override
	public final void e(final NBTTagCompound nbttagcompound) {
	}

	@Override
	public final boolean isInvulnerable(final DamageSource source) {
		return true;
	}

	@Override
	public final void setCustomName(final String customName) {
	}

	@Override
	public final void setCustomNameVisible(final boolean visible) {
	}

	@Override
	public final boolean a(final EntityHuman human, final Vec3D vec3d) {
		return true;
	}

	@Override
	public final boolean d(final int i, final ItemStack item) {
		return false;
	}

	@Override
	public final void setEquipment(final int i, final ItemStack item) {
	}

	@Override
	public final void a(final AxisAlignedBB boundingBox) {
	}

	@Override
	public final int getId() {
		return super.getId();
	}

	@Override
	public final void t_() {
		if (!this.lockTick)
			super.t_();
	}

	@Override
	public final void makeSound(final String sound, final float f1, final float f2) {
	}

	public final void setCustomNameNMS(String name) {
		if ((name != null) && (name.length() > 300)) {
			name = name.substring(0, 300);
		}
		super.setCustomName(name);
		super.setCustomNameVisible((name != null) && (!name.isEmpty()));
	}

	public final String getCustomNameNMS() {
		return super.getCustomName();
	}

	public final void setLockTick(final boolean lock) {
		this.lockTick = lock;
	}

	@Override
	public final void die() {
		setLockTick(false);
		super.die();
	}

	@Override
	public final CraftEntity getBukkitEntity() {
		return this.bukkitEntity == null ? new CraftNMSArmorStand(this.world.getServer(), this) : this.bukkitEntity;
	}

	public final void killEntityNMS() {
		die();
	}

	public final void setLocationNMS(final double x, final double y, final double z) {
		super.setPosition(x, y, z);

		final PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(getIdNMS(),
				MathHelper.floor(this.locX * 32.0D), MathHelper.floor(this.locY * 32.0D),
				MathHelper.floor(this.locZ * 32.0D), (byte) (int) (this.yaw * 256.0F / 360.0F),
				(byte) (int) (this.pitch * 256.0F / 360.0F), this.onGround);

		for (final Object obj : this.world.players) {
			if ((obj instanceof EntityPlayer)) {
				EntityPlayer nmsPlayer = (EntityPlayer) obj;

				final double distanceSquared = Maths.square(nmsPlayer.locX - this.locX)
						+ Maths.square(nmsPlayer.locZ - this.locZ);
				if ((distanceSquared < 8192.0D) && (nmsPlayer.playerConnection != null)) {
					nmsPlayer.playerConnection.sendPacket(teleportPacket);
				}
			}
		}
	}

	public final boolean isDeadNMS() {
		return this.dead;
	}

	public final int getIdNMS() {
		return super.getId();
	}

	public final CraftHologramLine getHologramLine() {
		return this.parentPiece;
	}

	public final Entity getBukkitEntityNMS() {
		return getBukkitEntity();
	}
}