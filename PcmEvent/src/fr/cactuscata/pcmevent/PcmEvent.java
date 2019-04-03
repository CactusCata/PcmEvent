package fr.cactuscata.pcmevent;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.cactuscata.pcmevent.command.nosimplecmd.config.ConfigCmd;
import fr.cactuscata.pcmevent.command.nosimplecmd.delayhit.DelayHitCmd;
import fr.cactuscata.pcmevent.command.nosimplecmd.experience.ExperienceCmd;
import fr.cactuscata.pcmevent.command.nosimplecmd.holo.HoloCmd;
import fr.cactuscata.pcmevent.command.nosimplecmd.permissions.PermissionsCmd;
import fr.cactuscata.pcmevent.command.nosimplecmd.physic.PhysicCmd;
import fr.cactuscata.pcmevent.command.nosimplecmd.physic.PhysicFile;
import fr.cactuscata.pcmevent.command.simplecmd.BroadcastCmd;
import fr.cactuscata.pcmevent.command.simplecmd.FlyCmd;
import fr.cactuscata.pcmevent.command.simplecmd.GamemodebCmd;
import fr.cactuscata.pcmevent.command.simplecmd.HealCmd;
import fr.cactuscata.pcmevent.command.simplecmd.KillbCmd;
import fr.cactuscata.pcmevent.command.simplecmd.LagCmd;
import fr.cactuscata.pcmevent.command.simplecmd.LolCmd;
import fr.cactuscata.pcmevent.command.simplecmd.SpawnCmd;
import fr.cactuscata.pcmevent.command.simplecmd.SpeedCmd;
import fr.cactuscata.pcmevent.command.simplecmd.inventoryviewver.OpenEnderCmd;
import fr.cactuscata.pcmevent.command.simplecmd.inventoryviewver.OpenInvCmd;
import fr.cactuscata.pcmevent.command.simplecmd.rank.RankFile;
import fr.cactuscata.pcmevent.command.simplecmd.rank.SetRankCmd;
import fr.cactuscata.pcmevent.command.simplecmd.spy.MsgCmd;
import fr.cactuscata.pcmevent.command.simplecmd.spy.RCmd;
import fr.cactuscata.pcmevent.command.simplecmd.spy.SocialSpyCmd;
import fr.cactuscata.pcmevent.command.simplecmd.spy.SpyListCmd;
import fr.cactuscata.pcmevent.command.simplecmd.tp.TpbCmd;
import fr.cactuscata.pcmevent.command.simplecmd.tp.TpposCmd;
import fr.cactuscata.pcmevent.command.simplecmd.vanish.VanishCmd;
import fr.cactuscata.pcmevent.command.simplecmd.vanish.VanishFile;
import fr.cactuscata.pcmevent.command.simplecmd.warp.DelWarpCmd;
import fr.cactuscata.pcmevent.command.simplecmd.warp.SetWarpCmd;
import fr.cactuscata.pcmevent.command.simplecmd.warp.WarpCmd;
import fr.cactuscata.pcmevent.command.simplecmd.warp.WarpFile;
import fr.cactuscata.pcmevent.command.simplecmd.warp.WarpInfoCmd;
import fr.cactuscata.pcmevent.command.simplecmd.warp.WarpsCmd;
import fr.cactuscata.pcmevent.listener.ChunkLoadListener;
import fr.cactuscata.pcmevent.listener.DeathListener;
import fr.cactuscata.pcmevent.listener.JoinGameListener;
import fr.cactuscata.pcmevent.listener.LeaveGameListener;
import fr.cactuscata.pcmevent.listener.MiscListeners;
import fr.cactuscata.pcmevent.listener.PlayerSendMessageListener;
import fr.cactuscata.pcmevent.listener.SoupEaterListener;
import fr.cactuscata.pcmevent.listener.VanishListener;
import fr.cactuscata.pcmevent.utils.bukkit.BungeeManager;
import fr.cactuscata.pcmevent.utils.bukkit.PlayerPcm;
import fr.cactuscata.pcmevent.utils.custominventory.InventoryHelper;
import fr.cactuscata.pcmevent.utils.experience.ExperienceClassementSystem;
import fr.cactuscata.pcmevent.utils.hologram.HoloFile;
import fr.cactuscata.pcmevent.utils.hologram.nms.NmsManagerImpl;
import fr.cactuscata.pcmevent.utils.hologram.objects.NamedHologramManager;
import fr.cactuscata.pcmevent.utils.hologram.task.HoloTask;
import fr.cactuscata.pcmevent.utils.permissions.PermissionsFile;
import fr.cactuscata.pcmevent.utils.sql.Sql;
import fr.cactuscata.pcmevent.utils.sql.TableEventPlayersSql;

/**
 * <p>
 * Classe principal du plugin, celle-ci permet toutes les initialisations comme
 * celle de la {@link Sql base de donnée}, de la {@link VanishFile
 * configuration} ou encore du fichier {@link WarpCmd warp} et bien d'autres
 * choses.
 * </p>
 * 
 * @author CactusCata
 * @version 2.6.0
 * @since 1.0.0
 */

public final class PcmEvent extends JavaPlugin {

	{
		PcmEvent.plugin = this;
	}

	private static Plugin plugin;
	private WarpFile warpFile;
	private HoloFile holoFile;
	private PhysicFile physicFile;
	private VanishFile vanishFile;
	private PermissionsFile permissionsFile;
	private final TableEventPlayersSql sql = new TableEventPlayersSql("alpha.pcm.ninja", "link", "event",
			"XMofwqR2E9jIcBrs");

	@Override
	public final void onEnable() {

		this.warpFile = new WarpFile();
		this.holoFile = new HoloFile();
		this.physicFile = new PhysicFile();
		this.vanishFile = new VanishFile();
		this.permissionsFile = new PermissionsFile();

		new BungeeManager();
		PlayerPcm.registerAllPlayer();

		new NmsManagerImpl();

		final InventoryHelper inv = new InventoryHelper();
		//Bukkit.getScheduler().runTaskTimer(this, new Tps(), 0L, 1L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new HoloTask(), 10L);

		this.registerEvents(super.getServer().getPluginManager(), new MiscListeners(inv, this.physicFile),
				new PlayerSendMessageListener(this.vanishFile), new JoinGameListener(inv), new LeaveGameListener(inv),
				new DeathListener(), new SoupEaterListener(), new ChunkLoadListener(),
				new VanishListener(this.vanishFile));

		super.getCommand("broadcast").setExecutor(new BroadcastCmd());
		super.getCommand("spawn").setExecutor(new SpawnCmd());
		super.getCommand("gamemodeb").setExecutor(new GamemodebCmd());
		super.getCommand("setrank").setExecutor(new SetRankCmd(new RankFile()));
		super.getCommand("setwarp").setExecutor(new SetWarpCmd(this.warpFile));
		super.getCommand("delwarp").setExecutor(new DelWarpCmd(this.warpFile));
		super.getCommand("warp").setExecutor(new WarpCmd(this.warpFile));
		super.getCommand("warps").setExecutor(new WarpsCmd(this.warpFile));
		super.getCommand("warpinfo").setExecutor(new WarpInfoCmd(this.warpFile));
		super.getCommand("socialspy").setExecutor(new SocialSpyCmd());
		super.getCommand("spylist").setExecutor(new SpyListCmd());
		super.getCommand("msg").setExecutor(new MsgCmd());
		super.getCommand("r").setExecutor(new RCmd());
		super.getCommand("speed").setExecutor(new SpeedCmd());
		super.getCommand("killb").setExecutor(new KillbCmd());
		super.getCommand("tppos").setExecutor(new TpposCmd());
		super.getCommand("tpb").setExecutor(new TpbCmd());
		super.getCommand("fly").setExecutor(new FlyCmd());
		super.getCommand("vanish").setExecutor(new VanishCmd());
		super.getCommand("config").setExecutor(new ConfigCmd(this.vanishFile));
		super.getCommand("lag").setExecutor(new LagCmd());
		super.getCommand("heal").setExecutor(new HealCmd());
		super.getCommand("openinv").setExecutor(new OpenInvCmd(inv));
		super.getCommand("openender").setExecutor(new OpenEnderCmd(inv));
		super.getCommand("hitdelay").setExecutor(new DelayHitCmd());
		super.getCommand("experience").setExecutor(new ExperienceCmd());
		super.getCommand("hologram").setExecutor(new HoloCmd(this.holoFile));
		super.getCommand("physic").setExecutor(new PhysicCmd(this.physicFile));
		super.getCommand("lol").setExecutor(new LolCmd());
		super.getCommand("permissions").setExecutor(new PermissionsCmd());

		ExperienceClassementSystem.registerExpInfo();
		// ExperienceClassementSystem.spawnNPC(this);

	}

	@Override
	public final void onDisable() {
		PlayerPcm.unregisterAllPlayer(false);
		this.sql.disconnect();
		// ExperienceClassementSystem.unregisterAllNPC();
		this.vanishFile.update();
		this.warpFile.update();
		this.physicFile.update();
		this.holoFile.update();
		this.permissionsFile.update();
		NamedHologramManager.getHolograms().forEach(holo -> holo.despawnEntities());
	}

	private final void registerEvents(final PluginManager pluginManager, final Listener... events) {
		for (final Listener listener : events)
			pluginManager.registerEvents(listener, this);
	}

	public static Plugin getPlugin() {
		return PcmEvent.plugin;
	}

}
