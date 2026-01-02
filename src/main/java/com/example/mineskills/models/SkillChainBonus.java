package com.example.mineskills.models;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public record SkillChainBonus(
    int level,
    Material icon,
    Component description,
    String effectType,
    double effectValue
) {
    public SkillChainBonus(int level, Material icon, Component description) {
        this(level, icon, description, null, 0);
    }
}
