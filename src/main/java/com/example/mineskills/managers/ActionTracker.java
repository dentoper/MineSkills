package com.example.mineskills.managers;

import com.example.mineskills.models.ActionProgress;
import com.example.mineskills.models.PlayerSkillData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ActionTracker {
    private final PlayerDataManager dataManager;
    private final FileConfiguration config;
    private final int oreBlocksForPoint;
    private final int normalBlocksForPoint;
    private final int damageForPoint;
    private final int blocksForPoint;
    private final int jumpsForPoint;

    public ActionTracker(PlayerDataManager dataManager, FileConfiguration config) {
        this.dataManager = dataManager;
        this.config = config;
        this.oreBlocksForPoint = config.getInt("action-tracking.mining.ore_blocks_for_point", 100);
        this.normalBlocksForPoint = config.getInt("action-tracking.mining.normal_blocks_for_point", 50);
        this.damageForPoint = config.getInt("action-tracking.combat.damage_for_point", 50);
        this.blocksForPoint = config.getInt("action-tracking.movement.blocks_for_point", 1000);
        this.jumpsForPoint = config.getInt("action-tracking.movement.jumps_for_point", 50);
    }

    public void trackMining(Player player, Block block) {
        PlayerSkillData data = dataManager.getOrCreatePlayerData(player);
        int currentProgress = data.actionProgress().mining();
        int newProgress = currentProgress + 1;

        int pointsToAdd = 0;

        if (isOre(block.getType())) {
            int miningLevel = data.getSkillLevel("MINING");
            if (miningLevel < 5 && newProgress >= oreBlocksForPoint) {
                pointsToAdd++;
                newProgress = 0;
            }
        } else {
            int fastMiningLevel = data.getSkillLevel("FAST_MINING");
            if (fastMiningLevel < 5 && newProgress >= normalBlocksForPoint) {
                pointsToAdd++;
                newProgress = 0;
            }
        }

        ActionProgress updatedProgress = data.actionProgress().withMining(newProgress);
        PlayerSkillData updatedData = data.updateActionProgress(updatedProgress);

        if (pointsToAdd > 0) {
            updatedData = updatedData.addSkillPoints(pointsToAdd);
            player.sendMessage(net.kyori.adventure.text.Component.text()
                .append(net.kyori.adventure.text.Component.text("✓ ").color(net.kyori.adventure.text.format.NamedTextColor.GREEN))
                .append(net.kyori.adventure.text.Component.text("You earned " + pointsToAdd + " skill point(s) from mining!").color(net.kyori.adventure.text.format.NamedTextColor.YELLOW))
            );
        }

        dataManager.updatePlayerData(updatedData);
    }

    public void trackCombat(Player player, double damage) {
        PlayerSkillData data = dataManager.getOrCreatePlayerData(player);
        int currentProgress = data.actionProgress().combat();
        int newProgress = (int) (currentProgress + damage);

        int powerBlowLevel = data.getSkillLevel("POWER_BLOW");
        int pointsToAdd = 0;

        if (powerBlowLevel < 5 && newProgress >= damageForPoint) {
            pointsToAdd = newProgress / damageForPoint;
            newProgress = newProgress % damageForPoint;
        }

        ActionProgress updatedProgress = data.actionProgress().withCombat(newProgress);
        PlayerSkillData updatedData = data.updateActionProgress(updatedProgress);

        if (pointsToAdd > 0) {
            updatedData = updatedData.addSkillPoints(pointsToAdd);
            player.sendMessage(net.kyori.adventure.text.Component.text()
                .append(net.kyori.adventure.text.Component.text("✓ ").color(net.kyori.adventure.text.format.NamedTextColor.GREEN))
                .append(net.kyori.adventure.text.Component.text("You earned " + pointsToAdd + " skill point(s) from combat!").color(net.kyori.adventure.text.format.NamedTextColor.YELLOW))
            );
        }

        dataManager.updatePlayerData(updatedData);
    }

    public void trackMovement(Player player, double distance, boolean isJump) {
        PlayerSkillData data = dataManager.getOrCreatePlayerData(player);
        ActionProgress currentProgress = data.actionProgress();
        int newCombat = currentProgress.combat();
        int newMining = currentProgress.mining();
        int newMovement = (int) (currentProgress.movement() + distance);

        int pointsToAdd = 0;

        if (isJump) {
            int swiftMovementLevel = data.getSkillLevel("SWIFT_MOVEMENT");
            if (swiftMovementLevel < 5) {
                newMovement += jumpsForPoint;
            }
        }

        int swiftMovementLevel = data.getSkillLevel("SWIFT_MOVEMENT");
        if (swiftMovementLevel < 5 && newMovement >= blocksForPoint) {
            pointsToAdd = newMovement / blocksForPoint;
            newMovement = newMovement % blocksForPoint;
        }

        ActionProgress updatedProgress = new ActionProgress(newCombat, newMining, newMovement);
        PlayerSkillData updatedData = data.updateActionProgress(updatedProgress);

        if (pointsToAdd > 0) {
            updatedData = updatedData.addSkillPoints(pointsToAdd);
            player.sendMessage(net.kyori.adventure.text.Component.text()
                .append(net.kyori.adventure.text.Component.text("✓ ").color(net.kyori.adventure.text.format.NamedTextColor.GREEN))
                .append(net.kyori.adventure.text.Component.text("You earned " + pointsToAdd + " skill point(s) from movement!").color(net.kyori.adventure.text.format.NamedTextColor.YELLOW))
            );
        }

        dataManager.updatePlayerData(updatedData);
    }

    private boolean isOre(Material material) {
        return material == Material.COAL_ORE ||
               material == Material.DEEPSLATE_COAL_ORE ||
               material == Material.IRON_ORE ||
               material == Material.DEEPSLATE_IRON_ORE ||
               material == Material.COPPER_ORE ||
               material == Material.DEEPSLATE_COPPER_ORE ||
               material == Material.GOLD_ORE ||
               material == Material.DEEPSLATE_GOLD_ORE ||
               material == Material.DIAMOND_ORE ||
               material == Material.DEEPSLATE_DIAMOND_ORE ||
               material == Material.REDSTONE_ORE ||
               material == Material.DEEPSLATE_REDSTONE_ORE ||
               material == Material.LAPIS_ORE ||
               material == Material.DEEPSLATE_LAPIS_ORE ||
               material == Material.EMERALD_ORE ||
               material == Material.DEEPSLATE_EMERALD_ORE ||
               material == Material.NETHER_GOLD_ORE ||
               material == Material.NETHER_QUARTZ_ORE ||
               material == Material.ANCIENT_DEBRIS;
    }
}
