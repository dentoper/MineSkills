package com.example.mineskills.listeners;

import com.example.mineskills.MineSkillsPlugin;
import com.example.mineskills.managers.ActionTracker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CombatActionListener implements Listener {
    private final MineSkillsPlugin plugin;
    private final ActionTracker actionTracker;

    public CombatActionListener(MineSkillsPlugin plugin) {
        this.plugin = plugin;
        this.actionTracker = plugin.getActionTracker();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        
        Player player = (Player) event.getDamager();
        double damage = event.getFinalDamage();
        
        actionTracker.trackCombat(player, damage);
    }
}
