package com.example.mineskills.managers;

import com.example.mineskills.models.ActionProgress;
import com.example.mineskills.models.PlayerSkillData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private final File dataFile;
    private final Map<UUID, PlayerSkillData> playerDataMap;

    public PlayerDataManager(File dataFolder) {
        this.dataFile = new File(dataFolder, "players.yml");
        this.playerDataMap = new HashMap<>();
        loadData();
    }

    private void loadData() {
        if (!dataFile.exists()) {
            try {
                dataFile.getParentFile().mkdirs();
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        
        if (config.contains("players")) {
            for (String uuidString : config.getConfigurationSection("players").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    String name = config.getString("players." + uuidString + ".name");
                    int skillPoints = config.getInt("players." + uuidString + ".skill_points");
                    int totalPoints = config.getInt("players." + uuidString + ".total_points");
                    
                    Map<String, Integer> skills = new HashMap<>();
                    if (config.contains("players." + uuidString + ".skills")) {
                        for (String skillId : config.getConfigurationSection("players." + uuidString + ".skills").getKeys(false)) {
                            int level = config.getInt("players." + uuidString + ".skills." + skillId);
                            skills.put(skillId, level);
                        }
                    }

                    int combat = config.getInt("players." + uuidString + ".action_progress.combat", 0);
                    int mining = config.getInt("players." + uuidString + ".action_progress.mining", 0);
                    int movement = config.getInt("players." + uuidString + ".action_progress.movement", 0);
                    ActionProgress actionProgress = new ActionProgress(combat, mining, movement);

                    PlayerSkillData data = new PlayerSkillData(uuid, name, skillPoints, totalPoints, skills, actionProgress);
                    playerDataMap.put(uuid, data);
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid UUID in player data: " + uuidString);
                }
            }
        }
    }

    public void saveData() {
        FileConfiguration config = new YamlConfiguration();
        
        for (PlayerSkillData data : playerDataMap.values()) {
            String path = "players." + data.uuid() + ".";
            config.set(path + "name", data.name());
            config.set(path + "skill_points", data.skillPoints());
            config.set(path + "total_points", data.totalPoints());
            
            for (Map.Entry<String, Integer> entry : data.skills().entrySet()) {
                config.set(path + "skills." + entry.getKey(), entry.getValue());
            }
            
            config.set(path + "action_progress.combat", data.actionProgress().combat());
            config.set(path + "action_progress.mining", data.actionProgress().mining());
            config.set(path + "action_progress.movement", data.actionProgress().movement());
        }

        try {
            config.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PlayerSkillData getPlayerData(UUID uuid) {
        return playerDataMap.get(uuid);
    }

    public PlayerSkillData getOrCreatePlayerData(Player player) {
        return playerDataMap.computeIfAbsent(player.getUniqueId(), uuid -> PlayerSkillData.create(player));
    }

    public void updatePlayerData(PlayerSkillData data) {
        playerDataMap.put(data.uuid(), data);
    }

    public void removePlayerData(UUID uuid) {
        playerDataMap.remove(uuid);
    }
}
