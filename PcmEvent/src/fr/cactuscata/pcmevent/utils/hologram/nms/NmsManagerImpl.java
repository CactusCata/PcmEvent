package fr.cactuscata.pcmevent.utils.hologram.nms;

import java.lang.reflect.Method;

import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

import fr.cactuscata.pcmevent.utils.hologram.objects.lines.CraftHologramLine;
import fr.cactuscata.pcmevent.utils.other.Reflection;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldServer;

public class NmsManagerImpl {
	private Method validateEntityMethod;
	private static NmsManagerImpl instance;

	public NmsManagerImpl() {
		instance = this;
	}

	public final void setup() throws Exception {
		registerCustomEntity(EntityNMSArmorStand.class, "ArmorStand", 30);

		this.validateEntityMethod = World.class.getDeclaredMethod("a", new Class[] { Entity.class });
		this.validateEntityMethod.setAccessible(true);

	}

	public final void registerCustomEntity(final Class<?> entityClass, final String name, final int id)
			throws Exception {
		Reflection.putInPrivateStaticMap(EntityTypes.class, "d", entityClass, name);
		Reflection.putInPrivateStaticMap(EntityTypes.class, "f", entityClass, id);
	}

	public final EntityNMSArmorStand spawnNMSArmorStand(final org.bukkit.World world, final double x, final double y,
			final double z, final CraftHologramLine parentPiece) {
		final WorldServer nmsWorld = ((CraftWorld) world).getHandle();
		final EntityNMSArmorStand invisibleArmorStand = new EntityNMSArmorStand(nmsWorld, parentPiece);
		invisibleArmorStand.setLocationNMS(x, y, z);
		addEntityToWorld(nmsWorld, invisibleArmorStand);

		return invisibleArmorStand;
	}

	private final void addEntityToWorld(final WorldServer nmsWorld, final Entity nmsEntity) {

		if (this.validateEntityMethod == null) {
			nmsWorld.addEntity(nmsEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
			return;
		}

		final int chunkX = MathHelper.floor(nmsEntity.locX / 16.0D), chunkZ = MathHelper.floor(nmsEntity.locZ / 16.0D);

		if (!nmsWorld.chunkProviderServer.isChunkLoaded(chunkX, chunkZ)) {
			nmsEntity.dead = true;
			return;
		}

		nmsWorld.getChunkAt(chunkX, chunkZ).a(nmsEntity);
		nmsWorld.entityList.add(nmsEntity);
		try {
			this.validateEntityMethod.invoke(nmsWorld, new Object[] { nmsEntity });
		} catch (final Exception e) {
			e.printStackTrace();
			return;
		}
		return;
	}

	public final boolean isUnloadUnsure(final org.bukkit.Chunk bukkitChunk) {
		return bukkitChunk.getWorld().isChunkInUse(bukkitChunk.getX(), bukkitChunk.getZ());
	}

	public final static NmsManagerImpl getInstance() {
		return instance;
	}

}