package com.example.mineskills.models;

import java.util.List;

public record SkillChain(
    List<SkillChainBonus> bonuses
) {
    public SkillChainBonus getBonus(int level) {
        if (level <= 0 || level > bonuses.size()) {
            return null;
        }
        return bonuses.get(level - 1);
    }
}
