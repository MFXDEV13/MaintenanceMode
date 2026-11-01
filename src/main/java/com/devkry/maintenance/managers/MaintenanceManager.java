
package com.devkry.maintenance.managers;

import com.devkry.maintenance.MaintenanceMode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MaintenanceManager {

    private final MaintenanceMode plugin;
    private boolean enabled;

    private static final LegacyComponentSerializer COLOR =
            LegacyComponentSerializer.legacyAmpersand();

    public MaintenanceManager(MaintenanceMode plugin) {
        this.plugin = plugin;
        this.enabled = plugin.getConfig().getBoolean(
                "maintenance.enabled", false
        );
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        plugin.getConfig().set("maintenance.enabled", enabled);
        plugin.saveConfig();

        if (enabled && plugin.getConfig().getBoolean(
                "maintenance.kick-existing-players", true)) {
            kickNonBypassPlayers();
        }
    }

    public void reload() {
        plugin.reloadConfig();
        this.enabled = plugin.getConfig().getBoolean(
                "maintenance.enabled", false
        );
    }

    public boolean canBypass(Player player) {
        return player.hasPermission("maintenance.bypass");
    }

    public void kickNonBypassPlayers() {
        Component message = getMessage("messages.kick");

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!canBypass(player)) {
                player.kick(message);
            }
        }
    }

    public Component getMessage(String path) {
        String prefix = plugin.getConfig().getString(
                "messages.prefix", "&8[&cMaintenance&8] "
        );

        String message = plugin.getConfig().getString(
                path, "&cMaintenance sedang aktif."
        );

        return COLOR.deserialize(prefix + message);
    }
}
