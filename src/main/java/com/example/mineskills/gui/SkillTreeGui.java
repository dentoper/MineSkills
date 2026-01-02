package com.example.mineskills.gui;

import com.example.mineskills.MineSkillsPlugin;
import com.example.mineskills.managers.PlayerDataManager;
import com.example.mineskills.managers.SkillManager;
import com.example.mineskills.models.PlayerSkillData;
import com.example.mineskills.models.Skill;
import com.example.mineskills.models.SkillBranch;
import com.example.mineskills.utils.ItemBuilder;
import com.example.mineskills.utils.ProgressBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SkillTreeGui {
    private final MineSkillsPlugin plugin;
    private final Player player;
    private final SkillManager skillManager;
    private final PlayerDataManager dataManager;

    public SkillTreeGui(MineSkillsPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.skillManager = plugin.getSkillManager();
        this.dataManager = plugin.getPlayerDataManager();
    }

    public Inventory createInventory() {
        Inventory inv = Bukkit.createInventory(null, 54, 
            Component.text().color(NamedTextColor.GOLD)
                .append(Component.text("Skill Tree", TextDecoration.BOLD))
                .build()
        );

        PlayerSkillData data = dataManager.getOrCreatePlayerData(player);

        fillBackground(inv);
        fillSkillBranches(inv, data);
        fillInfoPanel(inv, data);
        fillBottomBar(inv);

        return inv;
    }

    private void fillBackground(Inventory inv) {
        ItemStack background = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
            .name(Component.empty())
            .build();

        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, background);
            }
        }
    }

    private void fillSkillBranches(Inventory inv, PlayerSkillData data) {
        for (SkillBranch branch : skillManager.getBranches()) {
            ItemStack branchIcon = createBranchIcon(branch, data);
            inv.setItem(branch.guiSlot(), branchIcon);

            int[] skillSlots = getSkillSlotsForBranch(branch.guiSlot());
            for (int i = 0; i < branch.skills().size() && i < skillSlots.length; i++) {
                Skill skill = branch.skills().get(i);
                int level = data.getSkillLevel(skill.id());
                inv.setItem(skillSlots[i], skill.createIcon(level));
            }
        }
    }

    private ItemStack createBranchIcon(SkillBranch branch, PlayerSkillData data) {
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        
        int totalLevels = 0;
        int maxLevels = branch.skills().size() * 5;
        
        for (Skill skill : branch.skills()) {
            totalLevels += data.getSkillLevel(skill.id());
        }
        
        lore.add(Component.text("Progress: ").color(NamedTextColor.GRAY)
            .append(ProgressBar.createColored(totalLevels, maxLevels, NamedTextColor.RED, NamedTextColor.YELLOW, NamedTextColor.GREEN)));
        lore.add(Component.empty());
        lore.add(Component.text("Skills in this branch:").color(NamedTextColor.YELLOW));
        
        for (Skill skill : branch.skills()) {
            int level = data.getSkillLevel(skill.id());
            lore.add(Component.text("  ").color(NamedTextColor.DARK_GRAY)
                .append(Component.text(level + "/" + skill.maxLevel()).color(level == skill.maxLevel() ? NamedTextColor.GREEN : NamedTextColor.WHITE))
                .append(Component.text(" ").color(NamedTextColor.DARK_GRAY))
                .append(skill.getDisplayName().decorate(TextDecoration.ITALIC)));
        }
        
        lore.add(Component.empty());

        return new ItemBuilder(branch.icon())
            .name(Component.text().color(branch.color())
                .append(Component.text(branch.name(), TextDecoration.BOLD))
                .build())
            .lore(lore.toArray(new Component[0]))
            .glow()
            .build();
    }

    private int[] getSkillSlotsForBranch(int branchSlot) {
        int row = branchSlot / 9;
        int[] slots = new int[3];
        
        for (int i = 0; i < 3; i++) {
            int offset = i + 1;
            slots[i] = branchSlot + offset;
        }
        
        return slots;
    }

    private void fillInfoPanel(Inventory inv, PlayerSkillData data) {
        ItemStack info = new ItemBuilder(Material.PLAYER_HEAD)
            .name(Component.text().color(NamedTextColor.WHITE)
                .append(Component.text("Your Stats", TextDecoration.BOLD))
                .build())
            .lore(
                Component.empty(),
                Component.text("Name: ").color(NamedTextColor.GRAY)
                    .append(Component.text(data.name()).color(NamedTextColor.WHITE)),
                Component.text("Skill Points: ").color(NamedTextColor.GRAY)
                    .append(Component.text(data.skillPoints()).color(NamedTextColor.GOLD)),
                Component.text("Total Earned: ").color(NamedTextColor.GRAY)
                    .append(Component.text(data.totalPoints()).color(NamedTextColor.GREEN)),
                Component.empty(),
                Component.text("Action Progress:").color(NamedTextColor.YELLOW),
                Component.text("  Combat: ").color(NamedTextColor.GRAY)
                    .append(ProgressBar.create(data.actionProgress().combat(), 50, NamedTextColor.RED)),
                Component.text("  Mining: ").color(NamedTextColor.GRAY)
                    .append(ProgressBar.create(data.actionProgress().mining(), 100, NamedTextColor.GOLD)),
                Component.text("  Movement: ").color(NamedTextColor.GRAY)
                    .append(ProgressBar.create(data.actionProgress().movement(), 1000, NamedTextColor.GREEN)),
                Component.empty()
            )
            .build();

        inv.setItem(4, info);
    }

    private void fillBottomBar(Inventory inv) {
        ItemStack close = new ItemBuilder(Material.BARRIER)
            .name(Component.text("Close", NamedTextColor.RED))
            .lore(Component.text("Click to close menu", NamedTextColor.GRAY))
            .build();

        ItemStack help = new ItemBuilder(Material.BOOK)
            .name(Component.text("Help", NamedTextColor.YELLOW))
            .lore(
                Component.text("Left Click: Upgrade skill", NamedTextColor.GRAY),
                Component.text("Right Click: View skill chain", NamedTextColor.GRAY),
                Component.empty(),
                Component.text("Earn skill points by:", NamedTextColor.YELLOW),
                Component.text("  • Mining ores", NamedTextColor.GRAY),
                Component.text("  • Combat", NamedTextColor.GRAY),
                Component.text("  • Movement", NamedTextColor.GRAY)
            )
            .build();

        ItemStack refresh = new ItemBuilder(Material.EMERALD)
            .name(Component.text("Refresh", NamedTextColor.GREEN))
            .lore(Component.text("Click to refresh menu", NamedTextColor.GRAY))
            .build();

        inv.setItem(45, close);
        inv.setItem(49, help);
        inv.setItem(53, refresh);
    }
}
