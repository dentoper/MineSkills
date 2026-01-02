package com.example.mineskills.listeners;

import com.example.mineskills.MineSkillsPlugin;
import com.example.mineskills.managers.PlayerDataManager;
import com.example.mineskills.managers.SkillApplier;
import com.example.mineskills.models.PlayerSkillData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final MineSkillsPlugin plugin;
    private final PlayerDataManager dataManager;
    private final SkillApplier skillApplier;

    public PlayerJoinListener(MineSkillsPlugin plugin) {
        this.plugin = plugin;
        this.dataManager = plugin.getPlayerDataManager();
        this.skillApplier = plugin.getSkillApplier();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        PlayerSkillData data = dataManager.getOrCreatePlayerData(player);
        dataManager.updatePlayerData(data);
        
        skillApplier.applySkills(player, data);
        
        player.sendMessage(Component.empty());
        player.sendMessage(Component.text()
            .append(Component.text("╔══════════════════════════╗").color(NamedTextColor.GOLD))
        );
        player.sendMessage(Component.text()
            .append(Component.text("║").color(NamedTextColor.GOLD))
            .append(Component.text(" Welcome to MineSkills! ").color(NamedTextColor.YELLOW))
            .append(Component.text("║").color(NamedTextColor.GOLD))
        );
        player.sendMessage(Component.text()
            .append(Component.text("╚══════════════════════════╝").color(NamedTextColor.GOLD))
        );
        player.sendMessage(Component.empty());
        player.sendMessage(Component.text("Type ").color(NamedTextColor.GRAY)
            .append(Component.text("/skilltree open").color(NamedTextColor.AQUA))
            .append(Component.text(" to open the skill menu!").color(NamedTextColor.GRAY))
        );
        player.sendMessage(Component.empty());
    }
}
