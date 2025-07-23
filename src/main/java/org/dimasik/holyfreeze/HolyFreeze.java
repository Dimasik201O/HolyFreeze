package org.dimasik.holyfreeze;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class HolyFreeze extends JavaPlugin {

    public static HashMap<UUID, Freeze> freezeHashMap = new HashMap<>();
    public HashMap<Player, StatusFreeze> statusFreezeHashMap = new HashMap<>();
    private PAPIFreeze papiFreeze;
    private CommandFreeze commandFreeze;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        commandFreeze = new CommandFreeze(this);
        getCommand("freezing").setExecutor(commandFreeze);
        getCommand("prova").setExecutor(commandFreeze);
        getCommand("afkstaff").setExecutor(commandFreeze);
        getServer().getPluginManager().registerEvents(new ListenerFreeze(this), this);
        papiFreeze = new PAPIFreeze(this);
        papiFreeze.register();

        new BukkitRunnable(){
            @Override
            public void run(){
                for(Player player : Bukkit.getOnlinePlayers()){
                    updateStatus(player);
                }
            }
        }.runTaskTimer(this, 0, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if(papiFreeze != null){
            papiFreeze.unregister();
        }
    }

    public void updateStatus(Player player){
        if(statusFreezeHashMap.get(player) != null){
            if(statusFreezeHashMap.get(player) == StatusFreeze.AFK){
                player.sendTitle("", "AFK", 0, 30, 0);
            }
            if(statusFreezeHashMap.get(player) == StatusFreeze.CHECKING){
                player.sendTitle("", "На проверке", 0, 30, 0);
            }
        }
    }
}
