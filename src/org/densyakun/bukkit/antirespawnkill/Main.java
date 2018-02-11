package org.densyakun.bukkit.antirespawnkill;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener, Runnable {
	public int maxtime = 3;
	public boolean onteleport = false;
	public HashMap<UUID, Integer> times = new HashMap<UUID, Integer>();
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		maxtime = getConfig().getInt("time", maxtime);
		onteleport = getConfig().getBoolean("onteleport", onteleport);
		getServer().getPluginManager().registerEvents(this, this);
		new Thread(this).start();
	}
	
	public boolean isDamageable(UUID uuid) {
		return a(uuid, new Date().getTime());
	}
	
	private boolean a(UUID uuid, long time) {
		return maxtime <= time - times.get(uuid);
	}
	
	@Override
	public void run() {
		while (isEnabled()) {
			getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				public void run() {
					Iterator<UUID> keys = times.keySet().iterator();
					while (keys.hasNext()) {
						UUID uuid = keys.next();
						if (a(uuid, new Date().getTime())) {
							times.remove(uuid);
						}
					}
				}
			});
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@EventHandler
	public void EntityDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && a(e.getEntity().getUniqueId(), new Date().getTime())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void PlayerRespawn(PlayerRespawnEvent e) {
		times.put(e.getPlayer().getUniqueId(), 0);
	}
	
	@EventHandler
	public void PlayerTeleport(PlayerTeleportEvent e) {
		times.put(e.getPlayer().getUniqueId(), 0);
	}
}
