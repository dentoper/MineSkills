package com.example.mineskills.listeners;

import com.example.mineskills.MineSkillsPlugin;
import com.example.mineskills.managers.ActionTracker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class MiningActionListener implements Listener {
    private final MineSkillsPlugin plugin;
    private final ActionTracker actionTracker;

    public MiningActionListener(MineSkillsPlugin plugin) {
        this.plugin = plugin;
        this.actionTracker = plugin.getActionTracker();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer() == null) return;
        
        actionTracker.trackMining(event.getPlayer(), event.getBlock());
    }
}
