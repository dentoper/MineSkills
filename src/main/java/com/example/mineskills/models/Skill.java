package com.example.mineskills.models;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public record Skill(
    String id,
    String name,
    String branchName,
    NamedTextColor color,
    Material icon,
    int cost,
    int maxLevel,
    SkillChain chain,
    Skill prerequisite
) {
    public ItemStack createIcon(int currentLevel) {
        ItemBuilder builder = new ItemBuilder(icon)
            .name(Component.text(name).color(color))
            .lore(getLore(currentLevel));

        return builder.build();
    }

    private Component[] getLore(int currentLevel) {
        SkillChainBonus nextBonus = chain.getBonus(currentLevel + 1);

        if (currentLevel >= maxLevel) {
            return new Component[] {
                Component.empty(),
                Component.text("✓ Maximum Level").color(NamedTextColor.GREEN),
                Component.text("Level: " + currentLevel + "/" + maxLevel).color(NamedTextColor.GRAY)
            };
        }

        return new Component[] {
            Component.empty(),
            Component.text("Current Level: " + currentLevel + "/" + maxLevel).color(NamedTextColor.GRAY),
            Component.empty(),
            Component.text("Next Bonus:").color(NamedTextColor.YELLOW),
            nextBonus.description().color(NamedTextColor.GRAY),
            Component.empty(),
            Component.text("Left Click: Upgrade (" + cost + " pts)").color(NamedTextColor.GOLD),
            Component.text("Right Click: View Chain").color(NamedTextColor.AQUA)
        };
    }

    public Component getDisplayName() {
        return Component.text(name).color(color);
    }
}
