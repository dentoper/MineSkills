package com.example.mineskills;

import com.example.mineskills.commands.CommandTabCompleter;
import com.example.mineskills.commands.SkillTreeCommand;
import com.example.mineskills.gui.GuiManager;
import com.example.mineskills.listeners.*;
import com.example.mineskills.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineSkillsPlugin extends JavaPlugin {
    private SkillManager skillManager;
    private PlayerDataManager playerDataManager;
    private SkillApplier skillApplier;
    private ActionTracker actionTracker;
    private GuiManager guiManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        skillManager = new SkillManager();
        playerDataManager = new PlayerDataManager(getDataFolder());
        skillApplier = new SkillApplier();
        actionTracker = new ActionTracker(playerDataManager, getConfig());
        guiManager = new GuiManager(this);

        registerListeners();
        registerCommands();
        startAutoSaveTask();

        getLogger().info("MineSkills has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        playerDataManager.saveData();
        getLogger().info("MineSkills has been disabled!");
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new GuiClickListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MiningActionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CombatActionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MovementActionListener(this), this);
    }

    private void registerCommands() {
        SkillTreeCommand command = new SkillTreeCommand(this);
        getCommand("skilltree").setExecutor(command);
        getCommand("skilltree").setTabCompleter(new CommandTabCompleter());
    }

    private void startAutoSaveTask() {
        long interval = getConfig().getLong("auto-save-interval", 5) * 60 * 20;
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            getLogger().info("Auto-saving player data...");
            playerDataManager.saveData();
        }, interval, interval);
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public SkillApplier getSkillApplier() {
        return skillApplier;
    }

    public ActionTracker getActionTracker() {
        return actionTracker;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }
}
