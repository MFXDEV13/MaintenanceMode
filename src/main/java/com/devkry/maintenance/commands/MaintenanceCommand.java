
package com.devkry.maintenance.commands;

import com.devkry.maintenance.MaintenanceMode;
import com.devkry.maintenance.managers.MaintenanceManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceCommand implements CommandExecutor, TabCompleter {

    private final MaintenanceMode plugin;
    private final MaintenanceManager manager;

    public MaintenanceCommand(
            MaintenanceMode plugin,
            MaintenanceManager manager
    ) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        if (!sender.hasPermission("maintenance.admin")) {
            sender.sendMessage(manager.getMessage("messages.no-permission"));
            return true;
        }

        if (args.length == 0) {
    sender.sendMessage(
            net.kyori.adventure.text.Component.text(
                    "Gunakan: /maintenance <on|off|status|reload>"
            )
    );
    return true;
}

        switch (args[0].toLowerCase()) {

            case "on" -> {
                if (manager.isEnabled()) {
                    sender.sendMessage(
                            manager.getMessage("messages.already-enabled")
                    );
                    return true;
                }

                manager.setEnabled(true);
                sender.sendMessage(
                        manager.getMessage("messages.enabled")
                );
            }

            case "off" -> {
                if (!manager.isEnabled()) {
                    sender.sendMessage(
                            manager.getMessage("messages.already-disabled")
                    );
                    return true;
                }

                manager.setEnabled(false);
                sender.sendMessage(
                        manager.getMessage("messages.disabled")
                );
            }

            case "status" -> {
    String status = manager.isEnabled()
            ? "&aAKTIF"
            : "&cNONAKTIF";

    sender.sendMessage(
            net.kyori.adventure.text.serializer.legacy
                    .LegacyComponentSerializer.legacyAmpersand()
                    .deserialize("&8[&cMaintenance&8] &7Status: " + status)
    );
}

            case "reload" -> {
                manager.reload();
                sender.sendMessage(
                        manager.getMessage("messages.reloaded")
                );
            }

            default -> sender.sendMessage(
                    net.kyori.adventure.text.Component.text(
                            "Sub-command tidak dikenal. Gunakan: /maintenance <on|off|status|reload>"
                    )
            );
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {
        if (!sender.hasPermission("maintenance.admin")) {
            return List.of();
        }

        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>(
                    List.of("on", "off", "status", "reload")
            );

            suggestions.removeIf(s ->
                    !s.startsWith(args[0].toLowerCase())
            );

            return suggestions;
        }

        return List.of();
    }
}
