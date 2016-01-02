package org.densyakun.bukkit.antirespawnkill;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
public class Main extends JavaPlugin implements Listener, Runnable {
	public int maxtime = 3;
	public List<UUIDAndTime> players = new ArrayList<UUIDAndTime>();
	@Override
	public void onEnable() {
		saveDefaultConfig();
		maxtime = getConfig().getInt("time", maxtime);
		getServer().getPluginManager().registerEvents(this, this);
		new Thread(this).start();
	}
	@Override
	public void run() {
		while (isEnabled()) {
			getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				public void run() {
					for (int a = 0; a < players.size();) {
						players.get(a).time++;
						if (maxtime <= players.get(a).time) {
							players.remove(a);
						} else {
							a++;
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
	public void EntityDamage(EntityDamageEvent e) {
		for (int a = 0; a < players.size(); a++) {
			if ((e.getEntity().getUniqueId().equals(players.get(a).uuid)) && (players.get(a).time < maxtime)) {
				e.setCancelled(true);
				break;
			}
		}
	}
	@EventHandler
	public void PlayerRespawn(PlayerRespawnEvent e) {
		for (int a = 0; a < players.size(); a++) {
			if (e.getPlayer().getUniqueId().equals(players.get(a).uuid)) {
				players.get(a).time = 0;
				return;
			}
		}
		players.add(new UUIDAndTime(e.getPlayer().getUniqueId()));
	}
}
