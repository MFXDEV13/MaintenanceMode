
package com.devkry.maintenance.managers;

import com.devkry.maintenance.MaintenanceMode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MaintenanceManager {

    private final MaintenanceMode plugin;
    private final File whitelistFile;
    private final YamlConfiguration whitelistConfig;

    private final Map<UUID, String> whitelist = new LinkedHashMap<>();

    private boolean enabled;

    private static final LegacyComponentSerializer COLOR =
            LegacyComponentSerializer.legacyAmpersand();

    public MaintenanceManager(MaintenanceMode plugin) {
        this.plugin = plugin;

        this.enabled = plugin.getConfig().getBoolean(
                "maintenance.enabled", false
        );

        this.whitelistFile = new File(
                plugin.getDataFolder(), "whitelist.yml"
        );

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        this.whitelistConfig = YamlConfiguration.loadConfiguration(
                whitelistFile
        );

        loadWhitelist();
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
            kickNonWhitelistedPlayers();
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

    public boolean isWhitelisted(UUID uuid) {
        return whitelist.containsKey(uuid);
    }

    public boolean addWhitelist(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();

        if (whitelist.containsKey(uuid)) {
            return false;
        }

        String name = player.getName() != null
                ? player.getName()
                : uuid.toString();

        whitelist.put(uuid, name);
        saveWhitelist();

        return true;
    }

    public boolean removeWhitelist(UUID uuid) {
        if (!whitelist.containsKey(uuid)) {
            return false;
        }

        whitelist.remove(uuid);
        saveWhitelist();

        return true;
    }

    public UUID findWhitelistUUID(String name) {
        for (Map.Entry<UUID, String> entry : whitelist.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(name)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public List<String> getWhitelistNames() {
        return new ArrayList<>(whitelist.values());
    }

    public int getWhitelistSize() {
        return whitelist.size();
    }

    private void loadWhitelist() {
        whitelist.clear();

        if (!whitelistConfig.contains("players")) {
            return;
        }

        for (String key : whitelistConfig.getConfigurationSection(
                "players").getKeys(false)) {

            try {
                UUID uuid = UUID.fromString(key);
                String name = whitelistConfig.getString(
                        "players." + key, uuid.toString()
                );

                whitelist.put(uuid, name);
            } catch (IllegalArgumentException exception) {
                plugin.getLogger().warning(
                        "UUID tidak valid di whitelist.yml: " + key
                );
            }
        }
    }

    private void saveWhitelist() {
        whitelistConfig.set("players", null);

        for (Map.Entry<UUID, String> entry : whitelist.entrySet()) {
            whitelistConfig.set(
                    "players." + entry.getKey(),
                    entry.getValue()
            );
        }

        try {
            whitelistConfig.save(whitelistFile);
        } catch (IOException exception) {
            plugin.getLogger().severe(
                    "Gagal menyimpan whitelist.yml: " +
                            exception.getMessage()
            );
        }
    }

    public void kickNonWhitelistedPlayers() {
        Component message = getMessage("messages.kick");

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!canBypass(player) &&
                    !isWhitelisted(player.getUniqueId())) {
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

    public Component getRawMessage(String message) {
        return COLOR.deserialize(message);
    }
}
