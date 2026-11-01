
package com.devkry.maintenance;

import com.devkry.maintenance.commands.MaintenanceCommand;
import com.devkry.maintenance.listeners.PlayerJoinListener;
import com.devkry.maintenance.managers.MaintenanceManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MaintenanceMode extends JavaPlugin {

    private MaintenanceManager maintenanceManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        maintenanceManager = new MaintenanceManager(this);

        getServer().getPluginManager().registerEvents(
                new PlayerJoinListener(maintenanceManager),
                this
        );

        MaintenanceCommand command =
                new MaintenanceCommand(this, maintenanceManager);

        if (getCommand("maintenance") != null) {
            getCommand("maintenance").setExecutor(command);
            getCommand("maintenance").setTabCompleter(command);
        } else {
            getLogger().severe("Command 'maintenance' tidak ditemukan di plugin.yml!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("MaintenanceMode berhasil diaktifkan!");
        getLogger().info("Maintenance status: " +
                (maintenanceManager.isEnabled() ? "ON" : "OFF"));
    }

    @Override
    public void onDisable() {
        getLogger().info("MaintenanceMode berhasil dinonaktifkan.");
    }

    public MaintenanceManager getMaintenanceManager() {
        return maintenanceManager;
    }
}
