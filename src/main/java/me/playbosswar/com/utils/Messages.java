package me.playbosswar.com.utils;

import me.playbosswar.com.CommandTimerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messages {
    private static final boolean debug = CommandTimerPlugin.getPlugin().getConfig().getBoolean("debug");

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Get plugin prefix
     * @return prefix
     */
    private static String getPrefix() {
        return "&a[CommandTimer] &f";
    }

    /**
     * Send a message to a player
     * @param player
     * @param message
     */
    public static void sendMessageToPlayer(Player player, String message) {
        player.sendMessage(colorize(getPrefix() + message));
    }

    /**
     * Send a message to the console
     * @param message
     */
    public static void sendConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(colorize(getPrefix() + message));
    }

    public static void sendDebugConsole(String message) {
        if(debug) {
            sendConsole(message);
        }
    }

    /**
     * Send a message to anyone
     * @param sender
     * @param message
     */
    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(colorize("&a[CommandTimer] &f" + message));
    }

    /**
     * Send need to be a player message to sender
     * @param sender
     */
    public static void sendNeedToBePlayer(CommandSender sender) {
        sender.sendMessage(colorize(getPrefix() + "&cYou need to be a player to do this"));
    }

    /**
     * Send message if player has no correct permission
     * @param player
     */
    public static void sendNoPermission(Player player) {
        player.sendMessage(colorize(getPrefix() + "&cYou don't have the right permission to do this."));
    }

    public static void sendNoPermission(CommandSender sender) {
        sender.sendMessage(colorize(getPrefix() + "&cYou don't have the right permission to do this."));
    }

    /**
     * Send message if IO writing failed
     * @param player
     */
    public static void sendFailedIO(Player player) {
        Messages.sendMessageToPlayer(player, "&cCould not update file on disk");
    }

    public static void sendHelpMessage(CommandSender sender) {
        Messages.sendMessage(sender, "§7--- §eCommandTimer Help §7---");
        Messages.sendMessage(sender, "§e/cmt help - §7Open help menu");
        Messages.sendMessage(sender, "§e/cmt activate <task> - §7Activate a task");
        Messages.sendMessage(sender, "§e/cmt deactivate <task> - §7Deactivate a task");
        Messages.sendMessage(sender, "§e/cmt execute <task> - §7Instantly execute a task");
    }
}
