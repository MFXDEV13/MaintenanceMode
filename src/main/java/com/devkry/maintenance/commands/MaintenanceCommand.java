
package com.devkry.maintenance.commands;

import com.devkry.maintenance.managers.MaintenanceManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MaintenanceCommand implements CommandExecutor, TabCompleter {

    private final MaintenanceManager manager;

    public MaintenanceCommand(MaintenanceManager manager) {
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
            sender.sendMessage(
                    manager.getMessage("messages.no-permission")
            );
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(
                    manager.getMessage("messages.usage")
            );
            return true;
        }

        switch (args[0].toLowerCase(Locale.ROOT)) {

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
                        manager.getRawMessage(
                                "&8[&cMaintenance&8] &7Maintenance: " +
                                        status +
                                        " &8| &7Whitelist: &f" +
                                        manager.getWhitelistSize()
                        )
                );
            }

            case "reload" -> {
                manager.reload();

                sender.sendMessage(
                        manager.getMessage("messages.reloaded")
                );
            }

            case "add" -> {
                if (!requirePlayerArgument(sender, args)) {
                    return true;
                }

                String name = args[1];

                Player onlinePlayer = Bukkit.getPlayerExact(name);
                OfflinePlayer target = onlinePlayer != null
                        ? onlinePlayer
                        : Bukkit.getOfflinePlayerIfCached(name);

                if (target == null || target.getName() == null) {
                    sendPlayerUnknown(sender);
                    return true;
                }

                if (!manager.addWhitelist(target)) {
                    sender.sendMessage(
                            manager.getRawMessage(
                                    "&8[&cMaintenance&8] &e" +
                                            target.getName() +
                                            " sudah ada di whitelist."
                            )
                    );
                    return true;
                }

                sender.sendMessage(
                        manager.getRawMessage(
                                "&8[&cMaintenance&8] &a" +
                                        target.getName() +
                                        " berhasil ditambahkan ke whitelist."
                        )
                );
            }

            case "remove" -> {
                if (!requirePlayerArgument(sender, args)) {
                    return true;
                }

                UUID uuid = manager.findWhitelistUUID(args[1]);

                if (uuid == null) {
                    sender.sendMessage(
                            manager.getRawMessage(
                                    "&8[&cMaintenance&8] &c" +
                                            args[1] +
                                            " tidak ditemukan di whitelist."
                            )
                    );
                    return true;
                }

                manager.removeWhitelist(uuid);

                sender.sendMessage(
                        manager.getRawMessage(
                                "&8[&cMaintenance&8] &a" +
                                        args[1] +
                                        " berhasil dihapus dari whitelist."
                        )
                );
            }

            case "list" -> {
                List<String> names = manager.getWhitelistNames();

                sender.sendMessage(
                        manager.getRawMessage(
                                "&8[&cMaintenance&8] &6Daftar Maintenance Whitelist:"
                        )
                );

                if (names.isEmpty()) {
                    sender.sendMessage(
                            manager.getMessage("messages.whitelist-list-empty")
                    );
                    return true;
                }

                for (String name : names) {
                    sender.sendMessage(
                            manager.getRawMessage("&7- &f" + name)
                    );
                }
            }

            default -> sender.sendMessage(
                    manager.getMessage("messages.unknown-command")
            );
        }

        return true;
    }

    private boolean requirePlayerArgument(
            CommandSender sender,
            String[] args
    ) {
        if (args.length < 2 || args[1].isBlank()) {
            sender.sendMessage(
                    manager.getMessage("messages.usage")
            );
            return false;
        }

        return true;
    }

    private void sendPlayerUnknown(CommandSender sender) {
        sender.sendMessage(
                manager.getMessage("messages.whitelist-player-unknown")
        );
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
            return filter(
                    List.of("on", "off", "status", "reload",
                            "add", "remove", "list"),
                    args[0]
            );
        }

        if (args.length == 2) {
            String subcommand = args[0].toLowerCase(Locale.ROOT);

            if (subcommand.equals("add")) {
                List<String> onlineNames = new ArrayList<>();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    onlineNames.add(player.getName());
                }

                return filter(onlineNames, args[1]);
            }

            if (subcommand.equals("remove")) {
                return filter(manager.getWhitelistNames(), args[1]);
            }
        }

        return List.of();
    }

    private List<String> filter(List<String> values, String input) {
        String prefix = input.toLowerCase(Locale.ROOT);

        return values.stream()
                .filter(value ->
                        value.toLowerCase(Locale.ROOT).startsWith(prefix)
                )
                .toList();
    }
}
