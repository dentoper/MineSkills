package com.example.mineskills.listeners;

import com.example.mineskills.MineSkillsPlugin;
import com.example.mineskills.gui.GuiManager;
import com.example.mineskills.managers.PlayerDataManager;
import com.example.mineskills.managers.SkillApplier;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final MineSkillsPlugin plugin;
    private final PlayerDataManager dataManager;
    private final SkillApplier skillApplier;
    private final GuiManager guiManager;

    public PlayerQuitListener(MineSkillsPlugin plugin) {
        this.plugin = plugin;
        this.dataManager = plugin.getPlayerDataManager();
        this.skillApplier = plugin.getSkillApplier();
        this.guiManager = plugin.getGuiManager();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        skillApplier.cleanup(event.getPlayer());
        guiManager.clearSubMenu(event.getPlayer());
        dataManager.removePlayerData(event.getPlayer().getUniqueId());
        dataManager.saveData();
    }
}
