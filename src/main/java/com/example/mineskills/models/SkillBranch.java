package com.example.mineskills.models;

import net.kyori.adventure.text.NamedTextColor;
import org.bukkit.Material;

import java.util.List;

public record SkillBranch(
    String id,
    String name,
    NamedTextColor color,
    Material icon,
    int guiSlot,
    List<Skill> skills
) {
    public Skill getSkill(String skillId) {
        return skills.stream()
            .filter(s -> s.id().equals(skillId))
            .findFirst()
            .orElse(null);
    }
}
