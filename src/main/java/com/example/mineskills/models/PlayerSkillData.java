package com.example.mineskills.models;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record PlayerSkillData(
    UUID uuid,
    String name,
    int skillPoints,
    int totalPoints,
    Map<String, Integer> skills,
    ActionProgress actionProgress
) {
    public static PlayerSkillData create(Player player) {
        return new PlayerSkillData(
            player.getUniqueId(),
            player.getName(),
            20,
            20,
            new HashMap<>(),
            ActionProgress.empty()
        );
    }

    public int getSkillLevel(String skillId) {
        return skills.getOrDefault(skillId, 0);
    }

    public PlayerSkillData updateSkill(String skillId, int level) {
        Map<String, Integer> newSkills = new HashMap<>(skills);
        newSkills.put(skillId, level);
        return new PlayerSkillData(uuid, name, skillPoints, totalPoints, newSkills, actionProgress);
    }

    public PlayerSkillData addSkillPoints(int points) {
        return new PlayerSkillData(
            uuid,
            name,
            skillPoints + points,
            totalPoints + points,
            skills,
            actionProgress
        );
    }

    public PlayerSkillData spendSkillPoints(int points) {
        return new PlayerSkillData(
            uuid,
            name,
            skillPoints - points,
            totalPoints,
            skills,
            actionProgress
        );
    }

    public PlayerSkillData updateActionProgress(ActionProgress progress) {
        return new PlayerSkillData(uuid, name, skillPoints, totalPoints, skills, progress);
    }
}
