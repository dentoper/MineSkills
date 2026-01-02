package com.example.mineskills.managers;

import com.example.mineskills.models.PlayerSkillData;
import com.example.mineskills.models.Skill;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class SkillApplier {
    private final Map<UUID, Integer> jumpCount = new HashMap<>();
    private final Map<UUID, Boolean> wasOnGround = new HashMap<>();

    public void applySkills(Player player, PlayerSkillData data) {
        applyStrengthSkills(player, data);
        applyAgilitySkills(player, data);
        applyEnduranceSkills(player, data);
        applyMiningSkills(player, data);
        applyWisdomSkills(player, data);
    }

    private void applyStrengthSkills(Player player, PlayerSkillData data) {
        int powerBlow = data.getSkillLevel("POWER_BLOW");
        int ironSkin = data.getSkillLevel("IRON_SKIN");
        int extraHealth = data.getSkillLevel("EXTRA_HEALTH");

        applyPowerBlow(player, powerBlow);
        applyIronSkin(player, ironSkin);
        applyExtraHealth(player, extraHealth);
    }

    private void applyPowerBlow(Player player, int level) {
        var modifiersToRemove = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)
            .getModifiers()
            .stream()
            .filter(m -> m.getName().equals("mineskills_power_blow"))
            .collect(Collectors.toList());

        modifiersToRemove.forEach(m -> player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).removeModifier(m));

        if (level > 0) {
            double bonus = 0;
            if (level >= 1) bonus += 1.0;
            if (level >= 2) bonus += 1.0;
            if (level >= 3) bonus += 0.5;
            if (level >= 4) bonus += 1.0;
            if (level >= 5) bonus += 1.0;

            AttributeModifier modifier = new AttributeModifier(
                UUID.nameUUIDFromBytes("mineskills_power_blow".getBytes()),
                "mineskills_power_blow",
                bonus,
                AttributeModifier.Operation.ADD_NUMBER
            );
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).addModifier(modifier);
        }
    }

    private void applyIronSkin(Player player, int level) {
        var modifiersToRemove = player.getAttribute(Attribute.GENERIC_ARMOR)
            .getModifiers()
            .stream()
            .filter(m -> m.getName().equals("mineskills_iron_skin"))
            .collect(Collectors.toList());

        modifiersToRemove.forEach(m -> player.getAttribute(Attribute.GENERIC_ARMOR).removeModifier(m));

        if (level > 0) {
            double armor = level * 0.5;
            AttributeModifier modifier = new AttributeModifier(
                UUID.nameUUIDFromBytes("mineskills_iron_skin".getBytes()),
                "mineskills_iron_skin",
                armor,
                AttributeModifier.Operation.ADD_NUMBER
            );
            player.getAttribute(Attribute.GENERIC_ARMOR).addModifier(modifier);
        }
    }

    private void applyExtraHealth(Player player, int level) {
        var modifiersToRemove = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)
            .getModifiers()
            .stream()
            .filter(m -> m.getName().equals("mineskills_extra_health"))
            .collect(Collectors.toList());

        modifiersToRemove.forEach(m -> player.getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(m));

        if (level > 0) {
            double[] healthBonus = {2, 5, 9, 13, 18};
            double bonus = healthBonus[level - 1];

            AttributeModifier modifier = new AttributeModifier(
                UUID.nameUUIDFromBytes("mineskills_extra_health".getBytes()),
                "mineskills_extra_health",
                bonus,
                AttributeModifier.Operation.ADD_NUMBER
            );
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(modifier);
        }
    }

    private void applyAgilitySkills(Player player, PlayerSkillData data) {
        int swiftMovement = data.getSkillLevel("SWIFT_MOVEMENT");
        int evasion = data.getSkillLevel("EVASION");

        applySwiftMovement(player, swiftMovement);
        applyEvasion(player, evasion);
    }

    private void applySwiftMovement(Player player, int level) {
        var modifiersToRemove = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
            .getModifiers()
            .stream()
            .filter(m -> m.getName().equals("mineskills_swift_movement"))
            .collect(Collectors.toList());

        modifiersToRemove.forEach(m -> player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(m));

        if (level > 0) {
            double baseSpeed = 0.1;
            double bonus = baseSpeed * (level * 0.1);

            AttributeModifier modifier = new AttributeModifier(
                UUID.nameUUIDFromBytes("mineskills_swift_movement".getBytes()),
                "mineskills_swift_movement",
                bonus,
                AttributeModifier.Operation.ADD_NUMBER
            );
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(modifier);
        }
    }

    private void applyEvasion(Player player, int level) {
        var modifiersToRemove = player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)
            .getModifiers()
            .stream()
            .filter(m -> m.getName().equals("mineskills_evasion"))
            .collect(Collectors.toList());

        modifiersToRemove.forEach(m -> player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).removeModifier(m));

        if (level > 0) {
            double toughness = level * 0.5;

            AttributeModifier modifier = new AttributeModifier(
                UUID.nameUUIDFromBytes("mineskills_evasion".getBytes()),
                "mineskills_evasion",
                toughness,
                AttributeModifier.Operation.ADD_NUMBER
            );
            player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).addModifier(modifier);
        }
    }

    private void applyEnduranceSkills(Player player, PlayerSkillData data) {
        int regeneration = data.getSkillLevel("REGENERATION");
        int nightVision = data.getSkillLevel("NIGHT_VISION");
        int stamina = data.getSkillLevel("STAMINA");

        applyRegeneration(player, regeneration);
        applyNightVision(player, nightVision);
        applyStamina(player, stamina);
    }

    private void applyRegeneration(Player player, int level) {
        player.removePotionEffect(PotionEffectType.REGENERATION);

        if (level > 0) {
            int amplifier;
            if (level <= 1) amplifier = 0;
            else if (level <= 3) amplifier = 1;
            else amplifier = 2;

            PotionEffect effect = new PotionEffect(
                PotionEffectType.REGENERATION,
                Integer.MAX_VALUE,
                amplifier,
                false,
                false
            );
            player.addPotionEffect(effect);
        }
    }

    private void applyNightVision(Player player, int level) {
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);

        if (level > 0) {
            PotionEffect effect = new PotionEffect(
                PotionEffectType.NIGHT_VISION,
                Integer.MAX_VALUE,
                0,
                false,
                false
            );
            player.addPotionEffect(effect);
        }
    }

    private void applyStamina(Player player, int level) {
        if (level > 0) {
            player.setSaturation(20);
        }
    }

    private void applyMiningSkills(Player player, PlayerSkillData data) {
        int fastMining = data.getSkillLevel("FAST_MINING");

        applyFastMining(player, fastMining);
    }

    private void applyFastMining(Player player, int level) {
        var modifiersToRemove = player.getAttribute(Attribute.PLAYER_BLOCK_BREAK_SPEED)
            .getModifiers()
            .stream()
            .filter(m -> m.getName().equals("mineskills_fast_mining"))
            .collect(Collectors.toList());

        modifiersToRemove.forEach(m -> player.getAttribute(Attribute.PLAYER_BLOCK_BREAK_SPEED).removeModifier(m));

        if (level > 0) {
            double bonus = level * 0.1;

            AttributeModifier modifier = new AttributeModifier(
                UUID.nameUUIDFromBytes("mineskills_fast_mining".getBytes()),
                "mineskills_fast_mining",
                bonus,
                AttributeModifier.Operation.ADD_SCALAR
            );
            player.getAttribute(Attribute.PLAYER_BLOCK_BREAK_SPEED).addModifier(modifier);
        }
    }

    private void applyWisdomSkills(Player player, PlayerSkillData data) {
        int magicShield = data.getSkillLevel("MAGIC_SHIELD");

        applyMagicShield(player, magicShield);
    }

    private void applyMagicShield(Player player, int level) {
        var modifiersToRemove = player.getAttribute(Attribute.GENERIC_ARMOR)
            .getModifiers()
            .stream()
            .filter(m -> m.getName().equals("mineskills_magic_shield"))
            .collect(Collectors.toList());

        modifiersToRemove.forEach(m -> player.getAttribute(Attribute.GENERIC_ARMOR).removeModifier(m));

        if (level > 0) {
            double armor = level * 0.5;

            AttributeModifier modifier = new AttributeModifier(
                UUID.nameUUIDFromBytes("mineskills_magic_shield".getBytes()),
                "mineskills_magic_shield",
                armor,
                AttributeModifier.Operation.ADD_NUMBER
            );
            player.getAttribute(Attribute.GENERIC_ARMOR).addModifier(modifier);
        }
    }

    public void handleJump(Player player) {
        int doubleJumpLevel = player.hasMetadata("doubleJumpLevel") ? 
            player.getMetadata("doubleJumpLevel").get(0).asInt() : 0;

        if (doubleJumpLevel > 0) {
            UUID uuid = player.getUniqueId();
            int currentJumps = jumpCount.getOrDefault(uuid, 0);

            if (!player.isOnGround()) {
                if (currentJumps < doubleJumpLevel) {
                    jumpCount.put(uuid, currentJumps + 1);
                    player.setVelocity(player.getVelocity().setY(0.5));
                }
            } else {
                jumpCount.put(uuid, 0);
            }
        }
    }

    public void handleMove(Player player) {
        int doubleJumpLevel = player.hasMetadata("doubleJumpLevel") ? 
            player.getMetadata("doubleJumpLevel").get(0).asInt() : 0;

        if (doubleJumpLevel > 0) {
            UUID uuid = player.getUniqueId();
            boolean previouslyOnGround = wasOnGround.getOrDefault(uuid, false);
            boolean currentlyOnGround = player.isOnGround();

            if (previouslyOnGround && !currentlyOnGround) {
                jumpCount.put(uuid, jumpCount.getOrDefault(uuid, 0) + 1);
            } else if (!previouslyOnGround && currentlyOnGround) {
                jumpCount.put(uuid, 0);
            }

            wasOnGround.put(uuid, currentlyOnGround);
        }
    }

    public void cleanup(Player player) {
        UUID uuid = player.getUniqueId();
        jumpCount.remove(uuid);
        wasOnGround.remove(uuid);
        player.removeMetadata("doubleJumpLevel", player.getServer().getPluginManager().getPlugin("MineSkills"));
    }
}
