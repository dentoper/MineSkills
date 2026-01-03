package com.example.mineskills.managers;

import com.example.mineskills.models.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class SkillManager {
    private final List<SkillBranch> branches;

    public SkillManager() {
        this.branches = new ArrayList<>();
        initializeSkillBranches();
    }

    private void initializeSkillBranches() {
        List<Skill> strengthSkills = createStrengthSkills();
        List<Skill> agilitySkills = createAgilitySkills();
        List<Skill> enduranceSkills = createEnduranceSkills();
        List<Skill> miningSkills = createMiningSkills();
        List<Skill> wisdomSkills = createWisdomSkills();

        branches.add(new SkillBranch("STRENGTH", "Strength", NamedTextColor.RED, Material.DIAMOND_SWORD, 10, strengthSkills));
        branches.add(new SkillBranch("AGILITY", "Agility", NamedTextColor.GREEN, Material.FEATHER, 19, agilitySkills));
        branches.add(new SkillBranch("ENDURANCE", "Endurance", NamedTextColor.BLUE, Material.SHIELD, 28, enduranceSkills));
        branches.add(new SkillBranch("MINING", "Mining", NamedTextColor.GOLD, Material.DIAMOND_PICKAXE, 37, miningSkills));
        branches.add(new SkillBranch("WISDOM", "Wisdom", NamedTextColor.LIGHT_PURPLE, Material.AMETHYST_SHARD, 46, wisdomSkills));
    }

    private List<Skill> createStrengthSkills() {
        List<Skill> skills = new ArrayList<>();

        Skill powerBlow = new Skill(
            "POWER_BLOW",
            "Power Blow",
            "STRENGTH",
            NamedTextColor.RED,
            Material.DIAMOND_SWORD,
            5,
            5,
            new SkillChain(List.of(
                new SkillChainBonus(1, Material.IRON_SWORD, Component.text("+1 Sword Damage")),
                new SkillChainBonus(2, Material.IRON_AXE, Component.text("+1 Axe Damage")),
                new SkillChainBonus(3, Material.DIAMOND, Component.text("+0.5 General Damage")),
                new SkillChainBonus(4, Material.IRON_SHOVEL, Component.text("+1 Shovel Damage")),
                new SkillChainBonus(5, Material.IRON_PICKAXE, Component.text("+1 Pickaxe Damage"))
            )),
            null
        );

        Skill ironSkin = new Skill(
            "IRON_SKIN",
            "Iron Skin",
            "STRENGTH",
            NamedTextColor.RED,
            Material.IRON_CHESTPLATE,
            10,
            5,
            new SkillChain(List.of(
                new SkillChainBonus(1, Material.CHAINMAIL_CHESTPLATE, Component.text("+0.5 Armor")),
                new SkillChainBonus(2, Material.IRON_CHESTPLATE, Component.text("+1.0 Armor")),
                new SkillChainBonus(3, Material.DIAMOND_CHESTPLATE, Component.text("+1.5 Armor")),
                new SkillChainBonus(4, Material.NETHERITE_CHESTPLATE, Component.text("+2.0 Armor")),
                new SkillChainBonus(5, Material.GOLDEN_APPLE, Component.text("+2.5 Armor"))
            )),
            null
        );

        Skill extraHealth = new Skill(
            "EXTRA_HEALTH",
            "Extra Health",
            "STRENGTH",
            NamedTextColor.RED,
            Material.RED_WOOL,
            7,
            5,
            new SkillChain(List.of(
                new SkillChainBonus(1, Material.REDSTONE, Component.text("+2 Max HP")),
                new SkillChainBonus(2, Material.GOLDEN_APPLE, Component.text("+3 Max HP")),
                new SkillChainBonus(3, Material.ENCHANTED_GOLDEN_APPLE, Component.text("+4 Max HP")),
                new SkillChainBonus(4, Material.TOTEM_OF_UNDYING, Component.text("+4 Max HP")),
                new SkillChainBonus(5, Material.BEACON, Component.text("+5 Max HP"))
            )),
            null
        );

        skills.add(powerBlow);
        skills.add(ironSkin);
        skills.add(extraHealth);
        return skills;
    }

    private List<Skill> createAgilitySkills() {
        List<Skill> skills = new ArrayList<>();

        Skill swiftMovement = new Skill(
            "SWIFT_MOVEMENT",
            "Swift Movement",
            "AGILITY",
            NamedTextColor.GREEN,
            Material.FEATHER,
            5,
            5,
            new SkillChain(List.of(
                new SkillChainBonus(1, Material.LEATHER_BOOTS, Component.text("+10% Speed")),
                new SkillChainBonus(2, Material.CHAINMAIL_BOOTS, Component.text("+20% Speed")),
                new SkillChainBonus(3, Material.IRON_BOOTS, Component.text("+30% Speed")),
                new SkillChainBonus(4, Material.DIAMOND_BOOTS, Component.text("+40% Speed")),
                new SkillChainBonus(5, Material.NETHERITE_BOOTS, Component.text("+50% Speed"))
            )),
            null
        );

        Skill doubleJump = new Skill(
            "DOUBLE_JUMP",
            "Double Jump",
            "AGILITY",
            NamedTextColor.GREEN,
            Material.SLIME_BALL,
            15,
            5,
            new SkillChain(List.of(
                new SkillChainBonus(1, Material.SLIME_BALL, Component.text("1 Mid-air Jump")),
                new SkillChainBonus(2, Material.SLIME_BLOCK, Component.text("2 Mid-air Jumps")),
                new SkillChainBonus(3, Material.HONEY_BLOCK, Component.text("3 Mid-air Jumps")),
                new SkillChainBonus(4, Material.PHANTOM_MEMBRANE, Component.text("4 Mid-air Jumps")),
                new SkillChainBonus(5, Material.ELYTRA, Component.text("5 Mid-air Jumps"))
            )),
            swiftMovement
        );

        Skill evasion = new Skill(
            "EVASION",
            "Evasion",
            "AGILITY",
            NamedTextColor.GREEN,
            Material.SHIELD,
            10,
            5,
            new SkillChain(List.of(
                new SkillChainBonus(1, Material.IRON_HELMET, Component.text("-5% Damage Taken")),
                new SkillChainBonus(2, Material.CHAINMAIL_LEGGINGS, Component.text("-10% Damage Taken")),
                new SkillChainBonus(3, Material.DIAMOND_LEGGINGS, Component.text("-15% Damage Taken")),
                new SkillChainBonus(4, Material.NETHERITE_LEGGINGS, Component.text("-20% Damage Taken")),
                new SkillChainBonus(5, Material.END_CRYSTAL, Component.text("-25% Damage Taken"))
            )),
            null
        );

        skills.add(swiftMovement);
        skills.add(doubleJump);
        skills.add(evasion);
        return skills;
    }

    private List<Skill> createEnduranceSkills() {
        List<Skill> skills = new ArrayList<>();

        Skill regeneration = new Skill(
            "REGENERATION",
            "Regeneration",
            "ENDURANCE",
            NamedTextColor.BLUE,
            Material.REDSTONE_BLOCK,
            12,
            5,
            new SkillChain(List.of(
                new SkillChainBonus(1, Material.GOLDEN_APPLE, Component.text("Regeneration I")),
                new SkillChainBonus(2, Material.ENCHANTED_GOLDEN_APPLE, Component.text("Regeneration II")),
                new SkillChainBonus(3, Material.BEACON, Component.text("Regeneration II")),
                new SkillChainBonus(4, Material.GOLDEN_CARROT, Component.text("Regeneration III")),
                new SkillChainBonus(5, Material.TOTEM_OF_UNDYING, Component.text("Regeneration III"))
            )),
            null
        );

        Skill nightVision = new Skill(
            "NIGHT_VISION",
            "Night Vision",
            "ENDURANCE",
            NamedTextColor.BLUE,
            Material.ENDER_EYE,
            8,
            5,
            new SkillChain(List.of(
                new SkillChainBonus(1, Material.TORCH, Component.text("Permanent Night Vision")),
                new SkillChainBonus(2, Material.LANTERN, Component.text("Permanent Night Vision")),
                new SkillChainBonus(3, Material.GLOWSTONE, Component.text("Permanent Night Vision")),
                new SkillChainBonus(4, Material.SEA_LANTERN, Component.text("Permanent Night Vision")),
                new SkillChainBonus(5, Material.END_ROD, Component.text("Permanent Night Vision"))
            )),
            null
        );

        Skill stamina = new Skill(
            "STAMINA",
            "Stamina",
            "ENDURANCE",
            NamedTextColor.BLUE,
            Material.GOLDEN_CARROT,
            6,
            5,
            new SkillChain(List.of(
                new SkillChainBonus(1, Material.BREAD, Component.text("-5% Exhaustion")),
                new SkillChainBonus(2, Material.COOKED_BEEF, Component.text("-10% Exhaustion")),
                new SkillChainBonus(3, Material.COOKED_PORKCHOP, Component.text("-15% Exhaustion")),
                new SkillChainBonus(4, Material.RABBIT_STEW, Component.text("-20% Exhaustion")),
                new SkillChainBonus(5, Material.GOLDEN_CARROT, Component.text("-25% Exhaustion"))
            )),
            null
        );

        skills.add(regeneration);
        skills.add(nightVision);
        skills.add(stamina);
        return skills;
    }

    private List<Skill> createMiningSkills() {
        List<Skill> skills = new ArrayList<>();

        Skill mining = new Skill(
            "MINING",
            "Mining",
            "MINING",
            NamedTextColor.GOLD,
            Material.DIAMOND_PICKAXE,
            0,
            5,
            new SkillChain(List.of(
                new SkillChainBonus(1, Material.COAL_ORE, Component.text("+10% XP from Mining")),
                new SkillChainBonus(2, Material.IRON_ORE, Component.text("+15% XP from Mining")),
                new SkillChainBonus(3, Material.COPPER_ORE, Component.text("+20% XP from Mining")),
                new SkillChainBonus(4, Material.GOLD_ORE, Component.text("+25% XP from Mining")),
                new SkillChainBonus(5, Material.DIAMOND_ORE, Component.text("+30% XP from Mining"))
            )),
            null
        );

        Skill fastMining = new Skill(
            "FAST_MINING",
            "Fast Mining",
            "MINING",
            NamedTextColor.GOLD,
            Material.GOLDEN_PICKAXE,
            6,
            5,
            new SkillChain(List.of(
                new SkillChainBonus(1, Material.STONE_PICKAXE, Component.text("+10% Mining Speed")),
                new SkillChainBonus(2, Material.IRON_PICKAXE, Component.text("+20% Mining Speed")),
                new SkillChainBonus(3, Material.DIAMOND_PICKAXE, Component.text("+30% Mining Speed")),
                new SkillChainBonus(4, Material.NETHERITE_PICKAXE, Component.text("+40% Mining Speed")),
                new SkillChainBonus(5, Material.DIAMOND_BLOCK, Component.text("+50% Mining Speed"))
            )),
            null
        );

        Skill oreFinder = new Skill(
            "ORE_FINDER",
            "Ore Finder",
            "MINING",
            NamedTextColor.GOLD,
            Material.GLOWSTONE,
            8,
            5,
            new SkillChain(List.of(
                new SkillChainBonus(1, Material.GLOWSTONE_DUST, Component.text("Highlight Nearby Ores (Range 1)")),
                new SkillChainBonus(2, Material.GLOWSTONE, Component.text("Highlight Nearby Ores (Range 2)")),
                new SkillChainBonus(3, Material.SEA_LANTERN, Component.text("Highlight Nearby Ores (Range 3)")),
                new SkillChainBonus(4, Material.END_ROD, Component.text("Highlight Nearby Ores (Range 4)")),
                new SkillChainBonus(5, Material.LIGHT, Component.text("Highlight Nearby Ores (Range 5)"))
            )),
            null
        );

        skills.add(mining);
        skills.add(fastMining);
        skills.add(oreFinder);
        return skills;
    }

    private List<Skill> createWisdomSkills() {
        List<Skill> skills = new ArrayList<>();

        Skill luck = new Skill(
            "LUCK",
            "Luck",
            "WISDOM",
            NamedTextColor.LIGHT_PURPLE,
            Material.GOLD_NUGGET,
            5,
            5,
            new SkillChain(List.of(
                new SkillChainBonus(1, Material.DANDELION, Component.text("+5% Better Loot Chance")),
                new SkillChainBonus(2, Material.POPPY, Component.text("+10% Better Loot Chance")),
                new SkillChainBonus(3, Material.COPPER_INGOT, Component.text("+15% Better Loot Chance")),
                new SkillChainBonus(4, Material.GOLD_INGOT, Component.text("+20% Better Loot Chance")),
                new SkillChainBonus(5, Material.DIAMOND, Component.text("+25% Better Loot Chance"))
            )),
            null
        );

        Skill experience = new Skill(
            "EXPERIENCE",
            "Experience",
            "WISDOM",
            NamedTextColor.LIGHT_PURPLE,
            Material.EXPERIENCE_BOTTLE,
            8,
            5,
            new SkillChain(List.of(
                new SkillChainBonus(1, Material.EXPERIENCE_BOTTLE, Component.text("+5% XP from All Actions")),
                new SkillChainBonus(2, Material.ENCHANTED_BOOK, Component.text("+10% XP from All Actions")),
                new SkillChainBonus(3, Material.ENCHANTING_TABLE, Component.text("+15% XP from All Actions")),
                new SkillChainBonus(4, Material.ANVIL, Component.text("+20% XP from All Actions")),
                new SkillChainBonus(5, Material.END_CRYSTAL, Component.text("+25% XP from All Actions"))
            )),
            null
        );

        Skill magicShield = new Skill(
            "MAGIC_SHIELD",
            "Magic Shield",
            "WISDOM",
            NamedTextColor.LIGHT_PURPLE,
            Material.AMETHYST_BLOCK,
            12,
            5,
            new SkillChain(List.of(
                new SkillChainBonus(1, Material.AMETHYST_SHARD, Component.text("5% Damage Absorption")),
                new SkillChainBonus(2, Material.AMETHYST_CLUSTER, Component.text("10% Damage Absorption")),
                new SkillChainBonus(3, Material.END_STONE, Component.text("15% Damage Absorption")),
                new SkillChainBonus(4, Material.OBSIDIAN, Component.text("20% Damage Absorption")),
                new SkillChainBonus(5, Material.CRYING_OBSIDIAN, Component.text("25% Damage Absorption"))
            )),
            null
        );

        skills.add(luck);
        skills.add(experience);
        skills.add(magicShield);
        return skills;
    }

    public List<SkillBranch> getBranches() {
        return branches;
    }

    public Skill getSkill(String skillId) {
        for (SkillBranch branch : branches) {
            Skill skill = branch.getSkill(skillId);
            if (skill != null) {
                return skill;
            }
        }
        return null;
    }
}
