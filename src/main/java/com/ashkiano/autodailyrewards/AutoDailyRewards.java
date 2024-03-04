package com.ashkiano.autodailyrewards;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AutoDailyRewards extends JavaPlugin implements Listener {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            public void run() {
                String playerName = event.getPlayer().getName();
                String today = dateFormat.format(new Date());
                String lastClaimed = getConfig().getString("rewards." + playerName, "");

                if (!today.equals(lastClaimed)) {
                    String rewardCommand = getConfig().getString("reward-command").replace("%player%", playerName);
                    getServer().dispatchCommand(getServer().getConsoleSender(), rewardCommand);
                    event.getPlayer().sendMessage("You have claimed your daily reward.");

                    getConfig().set("rewards." + playerName, today);
                    saveConfig();
                }
            }
        }.runTaskLater(this, 1200L);
    }
}
