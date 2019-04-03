package fr.cactuscata.pcmevent.utils.hologram.objects.lines;

import org.bukkit.World;

import fr.cactuscata.pcmevent.utils.hologram.api.lines.TextLine;
import fr.cactuscata.pcmevent.utils.hologram.nms.EntityNMSArmorStand;
import fr.cactuscata.pcmevent.utils.hologram.nms.NmsManagerImpl;
import fr.cactuscata.pcmevent.utils.hologram.objects.CraftHologram;

public final class CraftTextLine extends CraftHologramLine implements TextLine {
	private String text;
	private EntityNMSArmorStand nmsNameble;

	public CraftTextLine(final CraftHologram parent, final String text) {
		super(0.23D, parent);
		setText(text);
	}

	public final String getText() {
		return this.text;
	}

	public final void setText(final String text) {
		this.text = text;

		if (this.nmsNameble != null) {
			if ((text != null) && (!text.isEmpty())) {
				this.nmsNameble.setCustomNameNMS(text);

			} else {
				this.nmsNameble.setCustomNameNMS("");

			}
		}
	}

	public final void spawn(final World world, final double x, final double y, final double z) {
		super.spawn(world, x, y, z);

		this.nmsNameble = NmsManagerImpl.getInstance().spawnNMSArmorStand(world, x, y - 1.25D, z, this);

		if ((this.text != null) && (!this.text.isEmpty())) {
			this.nmsNameble.setCustomNameNMS(this.text);
		}

		this.nmsNameble.setLockTick(true);
	}

	public final void despawn() {
		super.despawn();

		if (this.nmsNameble != null) {
			this.nmsNameble.killEntityNMS();
			this.nmsNameble = null;
		}
	}

	public final void teleport(final double x, final double y, final double z) {

		if (this.nmsNameble != null) {
			this.nmsNameble.setLocationNMS(x, y - 1.25D, z);
		}
	}

	public final int[] getEntitiesIDs() {
		if (isSpawned()) {

			return new int[] { this.nmsNameble.getIdNMS() };
		}

		return new int[0];
	}

	public final EntityNMSArmorStand getNmsNameble() {
		return this.nmsNameble;
	}

	public final String toString() {
		return "CraftTextLine [text=" + this.text + "]";
	}
}