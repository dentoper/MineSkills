package com.example.mineskills.gui;

import com.example.mineskills.MineSkillsPlugin;
import com.example.mineskills.managers.PlayerDataManager;
import com.example.mineskills.models.PlayerSkillData;
import com.example.mineskills.models.Skill;
import com.example.mineskills.models.SkillChainBonus;
import com.example.mineskills.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SkillChainSubMenu {
    private final MineSkillsPlugin plugin;
    private final Player player;
    private final Skill skill;

    public SkillChainSubMenu(MineSkillsPlugin plugin, Player player, Skill skill) {
        this.plugin = plugin;
        this.player = player;
        this.skill = skill;
    }

    public Inventory createInventory() {
        PlayerDataManager dataManager = plugin.getPlayerDataManager();
        PlayerSkillData data = dataManager.getOrCreatePlayerData(player);
        int currentLevel = data.getSkillLevel(skill.id());

        Inventory inv = Bukkit.createInventory(null, 54,
            Component.text().color(skill.color())
                .append(Component.text(skill.name() + " Chain").decorate(TextDecoration.BOLD))
                .build()
        );

        fillBackground(inv);
        fillSkillLevels(inv, currentLevel);
        fillInfoPanel(inv, currentLevel);
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

    private void fillSkillLevels(Inventory inv, int currentLevel) {
        List<SkillChainBonus> bonuses = skill.chain().bonuses();
        int[] slots = {10, 12, 14, 16, 20, 24, 28, 32, 36, 40, 42, 44};

        for (int i = 0; i < bonuses.size() && i < slots.length; i++) {
            SkillChainBonus bonus = bonuses.get(i);
            int level = i + 1;
            boolean isUnlocked = currentLevel >= level;
            boolean isNextLevel = currentLevel == level - 1;

            ItemStack item = createSkillLevelIcon(bonus, level, isUnlocked, isNextLevel);
            inv.setItem(slots[i], item);
        }
    }

    private ItemStack createSkillLevelIcon(SkillChainBonus bonus, int level, boolean isUnlocked, boolean isNextLevel) {
        ItemBuilder builder = new ItemBuilder(bonus.icon());

        if (isUnlocked) {
            builder.name(Component.text()
                .append(Component.text("✓ ").color(NamedTextColor.GREEN))
                .append(Component.text("Level " + level).decorate(TextDecoration.BOLD).color(skill.color()))
                .build());
            builder.glow();
        } else if (isNextLevel) {
            builder.name(Component.text()
                .append(Component.text("→ ").color(NamedTextColor.YELLOW))
                .append(Component.text("Level " + level).decorate(TextDecoration.BOLD).color(skill.color()))
                .build());
        } else {
            builder.name(Component.text()
                .append(Component.text("✗ ").color(NamedTextColor.DARK_GRAY))
                .append(Component.text("Level " + level).decorate(TextDecoration.BOLD).color(NamedTextColor.DARK_GRAY))
                .build());
        }

        builder.lore(
            Component.empty(),
            bonus.description().color(isUnlocked ? NamedTextColor.WHITE : NamedTextColor.DARK_GRAY),
            Component.empty(),
            Component.text("Cost: ").color(NamedTextColor.GRAY)
                .append(Component.text(skill.cost() + " points").color(NamedTextColor.GOLD))
        );

        return builder.build();
    }

    private void fillInfoPanel(Inventory inv, int currentLevel) {
        ItemStack info = new ItemBuilder(Material.BOOK)
            .name(Component.text("Skill Information", NamedTextColor.WHITE))
            .lore(
                Component.empty(),
                Component.text("Skill: ").color(NamedTextColor.GRAY)
                    .append(skill.getDisplayName().decorate(TextDecoration.BOLD)),
                Component.text("Branch: ").color(NamedTextColor.GRAY)
                    .append(Component.text(skill.branchName()).color(skill.color())),
                Component.text("Current Level: ").color(NamedTextColor.GRAY)
                    .append(Component.text(currentLevel + "/" + skill.maxLevel()).color(
                        currentLevel == skill.maxLevel() ? NamedTextColor.GREEN : NamedTextColor.WHITE)),
                Component.empty(),
                Component.text("Progress to next level:").color(NamedTextColor.YELLOW),
                ProgressBar.create(currentLevel, skill.maxLevel(), skill.color()),
                Component.empty(),
                Component.text("Skill bonuses:").color(NamedTextColor.YELLOW),
                Component.text("Each level unlocks new bonuses!", NamedTextColor.GRAY),
                Component.text("Click on unlocked levels to upgrade.", NamedTextColor.GRAY),
                Component.empty()
            )
            .build();

        inv.setItem(4, info);
    }

    private void fillBottomBar(Inventory inv) {
        ItemStack back = new ItemBuilder(Material.ARROW)
            .name(Component.text("Back to Skill Tree", NamedTextColor.YELLOW))
            .lore(Component.text("Click to return", NamedTextColor.GRAY))
            .build();

        ItemStack upgrade = new ItemBuilder(Material.EMERALD_BLOCK)
            .name(Component.text("Upgrade Skill", NamedTextColor.GREEN))
            .lore(
                Component.text("Click to upgrade to next level", NamedTextColor.GRAY),
                Component.text("Cost: " + skill.cost() + " points", NamedTextColor.GOLD)
            )
            .build();

        inv.setItem(45, back);
        inv.setItem(53, upgrade);
    }
}
