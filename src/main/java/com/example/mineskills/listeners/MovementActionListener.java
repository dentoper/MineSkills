package com.example.mineskills.listeners;

import com.example.mineskills.MineSkillsPlugin;
import com.example.mineskills.managers.ActionTracker;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MovementActionListener implements Listener {
    private final MineSkillsPlugin plugin;
    private final ActionTracker actionTracker;
    private final Map<UUID, Location> lastLocations;

    public MovementActionListener(MineSkillsPlugin plugin) {
        this.plugin = plugin;
        this.actionTracker = plugin.getActionTracker();
        this.lastLocations = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        
        if (to == null) return;
        
        if (from.getX() == to.getX() && from.getY() == to.getY() && from.getZ() == to.getZ()) {
            return;
        }
        
        UUID uuid = player.getUniqueId();
        Location lastLoc = lastLocations.get(uuid);
        
        boolean isJump = false;
        if (lastLoc != null && !player.isOnGround() && lastLoc.getY() < to.getY()) {
            isJump = true;
        }
        
        double distance = from.distance(to);
        actionTracker.trackMovement(player, distance, isJump);
        
        lastLocations.put(uuid, to.clone());
    }
}
