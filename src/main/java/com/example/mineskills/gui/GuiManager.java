package com.example.mineskills.gui;

import com.example.mineskills.MineSkillsPlugin;
import com.example.mineskills.models.Skill;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuiManager {
    private final MineSkillsPlugin plugin;
    private final Map<UUID, Skill> openSubMenuSkills;

    public GuiManager(MineSkillsPlugin plugin) {
        this.plugin = plugin;
        this.openSubMenuSkills = new HashMap<>();
    }

    public void openSkillTree(Player player) {
        SkillTreeGui gui = new SkillTreeGui(plugin, player);
        Inventory inv = gui.createInventory();
        player.openInventory(inv);
    }

    public void openSkillSubMenu(Player player, Skill skill) {
        SkillChainSubMenu gui = new SkillChainSubMenu(plugin, player, skill);
        Inventory inv = gui.createInventory();
        player.openInventory(inv);
        openSubMenuSkills.put(player.getUniqueId(), skill);
    }

    public Skill getOpenSubMenuSkill(UUID uuid) {
        return openSubMenuSkills.get(uuid);
    }

    public void clearSubMenu(Player player) {
        openSubMenuSkills.remove(player.getUniqueId());
    }
}
