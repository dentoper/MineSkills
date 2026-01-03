package com.example.mineskills.listeners;

import com.example.mineskills.MineSkillsPlugin;
import com.example.mineskills.gui.GuiManager;
import com.example.mineskills.gui.SkillChainSubMenu;
import com.example.mineskills.gui.SkillTreeGui;
import com.example.mineskills.managers.PlayerDataManager;
import com.example.mineskills.managers.SkillApplier;
import com.example.mineskills.models.PlayerSkillData;
import com.example.mineskills.models.Skill;
import com.example.mineskills.models.SkillBranch;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuiClickListener implements Listener {
    private final MineSkillsPlugin plugin;
    private final GuiManager guiManager;
    private final PlayerDataManager dataManager;
    private final SkillApplier skillApplier;

    public GuiClickListener(MineSkillsPlugin plugin) {
        this.plugin = plugin;
        this.guiManager = plugin.getGuiManager();
        this.dataManager = plugin.getPlayerDataManager();
        this.skillApplier = plugin.getSkillApplier();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        String title = LegacyComponentSerializer.legacySection().serialize(event.getView().title());

        if (title == null || !title.contains("Skill")) return;

        event.setCancelled(true);

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        ItemStack clicked = event.getCurrentItem();
        int slot = event.getSlot();

        if (title.contains("Chain")) {
            handleSubMenuClick(player, slot, clicked);
        } else {
            handleMainMenuClick(player, slot, clicked, event.isRightClick());
        }
    }

    private void handleMainMenuClick(Player player, int slot, ItemStack clicked, boolean isRightClick) {
        Material type = clicked.getType();

        if (type == Material.BARRIER && slot == 45) {
            player.closeInventory();
            return;
        }

        if (type == Material.EMERALD && slot == 53) {
            refreshMainMenu(player);
            return;
        }

        Skill skill = getSkillAtSlot(slot);
        if (skill != null) {
            if (isRightClick) {
                guiManager.openSkillSubMenu(player, skill);
            } else {
                tryUpgradeSkill(player, skill);
            }
        }
    }

    private void handleSubMenuClick(Player player, int slot, ItemStack clicked) {
        Skill currentSkill = guiManager.getOpenSubMenuSkill(player.getUniqueId());
        if (currentSkill == null) {
            player.closeInventory();
            return;
        }
        
        Material type = clicked.getType();
        
        if (type == Material.ARROW && slot == 45) {
            guiManager.clearSubMenu(player);
            guiManager.openSkillTree(player);
            return;
        }
        
        if (type == Material.EMERALD_BLOCK && slot == 53) {
            tryUpgradeSkill(player, currentSkill);
            guiManager.clearSubMenu(player);
            guiManager.openSkillSubMenu(player, currentSkill);
        }
        
        if (slot >= 10 && slot <= 44 && slot % 2 == 0) {
            tryUpgradeSkill(player, currentSkill);
            guiManager.clearSubMenu(player);
            guiManager.openSkillSubMenu(player, currentSkill);
        }
    }

    private void tryUpgradeSkill(Player player, Skill skill) {
        PlayerSkillData data = dataManager.getOrCreatePlayerData(player);
        int currentLevel = data.getSkillLevel(skill.id());
        
        if (currentLevel >= skill.maxLevel()) {
            player.sendMessage(Component.text("This skill is already at maximum level!", NamedTextColor.RED));
            return;
        }
        
        if (skill.prerequisite() != null) {
            int prereqLevel = data.getSkillLevel(skill.prerequisite().id());
            if (prereqLevel < 1) {
                player.sendMessage(Component.text("You need to unlock " + skill.prerequisite().name() + " first!", NamedTextColor.RED));
                return;
            }
        }
        
        if (data.skillPoints() < skill.cost()) {
            player.sendMessage(Component.text("Not enough skill points! You need " + skill.cost() + " points.", NamedTextColor.RED));
            return;
        }
        
        PlayerSkillData updatedData = data.spendSkillPoints(skill.cost());
        updatedData = updatedData.updateSkill(skill.id(), currentLevel + 1);
        dataManager.updatePlayerData(updatedData);
        
        skillApplier.applySkills(player, updatedData);
        
        player.sendMessage(Component.text()
            .append(Component.text("✓ ").color(NamedTextColor.GREEN))
            .append(skill.getDisplayName().color(NamedTextColor.WHITE))
            .append(Component.text(" upgraded to level " + (currentLevel + 1) + "!").color(NamedTextColor.GREEN))
        );
        
        refreshCurrentInventory(player);
    }

    private void refreshMainMenu(Player player) {
        SkillTreeGui gui = new SkillTreeGui(plugin, player);
        Inventory newInv = gui.createInventory();
        
        if (player.getOpenInventory().getTopInventory() != null) {
            player.getOpenInventory().getTopInventory().setContents(newInv.getContents());
        }
    }

    private void refreshCurrentInventory(Player player) {
        Skill currentSkill = guiManager.getOpenSubMenuSkill(player.getUniqueId());
        
        if (currentSkill != null) {
            guiManager.clearSubMenu(player);
            guiManager.openSkillSubMenu(player, currentSkill);
        } else {
            refreshMainMenu(player);
        }
    }

    private Skill getSkillAtSlot(int slot) {
        int[] rowSlots = {10, 11, 12, 19, 20, 21, 28, 29, 30, 37, 38, 39, 46, 47, 48};
        
        for (int i = 0; i < rowSlots.length; i++) {
            if (rowSlots[i] == slot) {
                int branchIndex = i / 3;
                int skillIndex = i % 3;
                
                if (branchIndex < plugin.getSkillManager().getBranches().size()) {
                    SkillBranch branch = plugin.getSkillManager().getBranches().get(branchIndex);
                    if (skillIndex < branch.skills().size()) {
                        return branch.skills().get(skillIndex);
                    }
                }
            }
        }
        
        return null;
    }
}
