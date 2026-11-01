
package com.devkry.maintenance.listeners;

import com.devkry.maintenance.managers.MaintenanceManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final MaintenanceManager manager;

    public PlayerJoinListener(MaintenanceManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!manager.isEnabled()) {
            return;
        }

        if (manager.canBypass(event.getPlayer())) {
            return;
        }

        if (manager.isWhitelisted(event.getPlayer().getUniqueId())) {
            return;
        }

        event.getPlayer().kick(
                manager.getMessage("messages.join")
        );
    }
}
