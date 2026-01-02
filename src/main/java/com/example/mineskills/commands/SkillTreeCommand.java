package com.example.mineskills.commands;

import com.example.mineskills.MineSkillsPlugin;
import com.example.mineskills.gui.GuiManager;
import com.example.mineskills.managers.PlayerDataManager;
import com.example.mineskills.models.PlayerSkillData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkillTreeCommand implements CommandExecutor {
    private final MineSkillsPlugin plugin;
    private final GuiManager guiManager;
    private final PlayerDataManager dataManager;

    public SkillTreeCommand(MineSkillsPlugin plugin) {
        this.plugin = plugin;
        this.guiManager = plugin.getGuiManager();
        this.dataManager = plugin.getPlayerDataManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            guiManager.openSkillTree((Player) sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "open":
                handleOpen(sender);
                break;
            case "info":
                handleInfo(sender);
                break;
            case "addpoints":
                handleAddPoints(sender, args);
                break;
            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void handleOpen(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("This command can only be used by players!", NamedTextColor.RED));
            return;
        }

        guiManager.openSkillTree((Player) sender);
    }

    private void handleInfo(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("This command can only be used by players!", NamedTextColor.RED));
            return;
        }

        Player player = (Player) sender;
        PlayerSkillData data = dataManager.getOrCreatePlayerData(player);

        player.sendMessage(Component.empty());
        player.sendMessage(Component.text("═══ MineSkills Info ═══", NamedTextColor.GOLD));
        player.sendMessage(Component.empty());
        player.sendMessage(Component.text("Player: ", NamedTextColor.GRAY)
            .append(Component.text(data.name(), NamedTextColor.WHITE)));
        player.sendMessage(Component.text("Skill Points: ", NamedTextColor.GRAY)
            .append(Component.text(data.skillPoints(), NamedTextColor.GOLD)));
        player.sendMessage(Component.text("Total Earned: ", NamedTextColor.GRAY)
            .append(Component.text(data.totalPoints(), NamedTextColor.GREEN)));
        player.sendMessage(Component.empty());
        player.sendMessage(Component.text("Action Progress:", NamedTextColor.YELLOW));
        player.sendMessage(Component.text("  Combat: ", NamedTextColor.GRAY)
            .append(Component.text(data.actionProgress().combat() + "/50", NamedTextColor.RED)));
        player.sendMessage(Component.text("  Mining: ", NamedTextColor.GRAY)
            .append(Component.text(data.actionProgress().mining() + "/100", NamedTextColor.GOLD)));
        player.sendMessage(Component.text("  Movement: ", NamedTextColor.GRAY)
            .append(Component.text(data.actionProgress().movement() + "/1000", NamedTextColor.GREEN)));
        player.sendMessage(Component.empty());
    }

    private void handleAddPoints(CommandSender sender, String[] args) {
        if (!sender.hasPermission("mineskills.admin")) {
            sender.sendMessage(Component.text("You don't have permission to use this command!", NamedTextColor.RED));
            return;
        }

        if (args.length < 3) {
            sender.sendMessage(Component.text("Usage: /skilltree addpoints <player> <amount>", NamedTextColor.RED));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(Component.text("Player not found!", NamedTextColor.RED));
            return;
        }

        try {
            int amount = Integer.parseInt(args[2]);
            if (amount <= 0) {
                sender.sendMessage(Component.text("Amount must be positive!", NamedTextColor.RED));
                return;
            }

            PlayerSkillData data = dataManager.getOrCreatePlayerData(target);
            PlayerSkillData updatedData = data.addSkillPoints(amount);
            dataManager.updatePlayerData(updatedData);

            sender.sendMessage(Component.text()
                .append(Component.text("✓ ").color(NamedTextColor.GREEN))
                .append(Component.text("Added ").color(NamedTextColor.GRAY))
                .append(Component.text(amount).color(NamedTextColor.GOLD))
                .append(Component.text(" skill points to ").color(NamedTextColor.GRAY))
                .append(Component.text(target.getName()).color(NamedTextColor.WHITE))
            );

            target.sendMessage(Component.text()
                .append(Component.text("✓ ").color(NamedTextColor.GREEN))
                .append(Component.text("You received ").color(NamedTextColor.GRAY))
                .append(Component.text(amount).color(NamedTextColor.GOLD))
                .append(Component.text(" skill points!").color(NamedTextColor.GRAY))
            );
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Invalid amount! Please enter a number.", NamedTextColor.RED));
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Component.empty());
        sender.sendMessage(Component.text("═══ MineSkills Commands ═══", NamedTextColor.GOLD));
        sender.sendMessage(Component.empty());
        sender.sendMessage(Component.text("/skilltree open", NamedTextColor.YELLOW)
            .append(Component.text(" - Open skill tree menu", NamedTextColor.GRAY)));
        sender.sendMessage(Component.text("/skilltree info", NamedTextColor.YELLOW)
            .append(Component.text(" - View your skill stats", NamedTextColor.GRAY)));
        if (sender.hasPermission("mineskills.admin")) {
            sender.sendMessage(Component.text("/skilltree addpoints <player> <amount>", NamedTextColor.YELLOW)
                .append(Component.text(" - Add skill points to player", NamedTextColor.GRAY)));
        }
        sender.sendMessage(Component.empty());
    }
}
