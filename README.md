# MineSkills

A comprehensive skill system plugin for Paper 1.21.x that enhances gameplay with RPG-style skill progression.

## Features

### 5 Skill Branches with 15 Unique Skills

#### 🗡️ Strength (Red)
- **Power Blow** (5 points) - Increase damage with various weapons
- **Iron Skin** (10 points) - Gain additional armor
- **Extra Health** (7 points) - Increase maximum health

#### 🏃 Agility (Green)
- **Swift Movement** (5 points) - Increase movement speed
- **Double Jump** (15 points) - Perform mid-air jumps (requires Swift Movement Lvl 1+)
- **Evasion** (10 points) - Reduce incoming damage

#### 🛡️ Endurance (Blue)
- **Regeneration** (12 points) - Permanent regeneration effect
- **Night Vision** (8 points) - Permanent night vision
- **Stamina** (6 points) - Reduce exhaustion rate

#### ⛏️ Mining (Gold)
- **Mining** (0 points, free on join) - Increase XP from mining
- **Fast Mining** (6 points) - Increase mining speed
- **Ore Finder** (8 points) - Highlight nearby ores

#### 💎 Wisdom (Purple)
- **Luck** (5 points) - Increase chance of better loot
- **Experience** (8 points) - Increase XP from all actions
- **Magic Shield** (12 points) - Damage absorption

### Action Tracking System

Earn skill points by performing various actions:

- **Mining**: Break ores to earn Mining skill points
- **Combat**: Deal damage to earn Power Blow skill points
- **Movement**: Travel distance and jump to earn Swift Movement skill points

### Key Features

- ✅ Modern Component API for all text elements (no legacy color codes)
- ✅ Instant GUI updates without reopening menus
- ✅ Skill chain system with 5 levels per skill
- ✅ Submenus for viewing detailed skill progressions
- ✅ Persistent data storage in `players.yml`
- ✅ Auto-save every 5 minutes
- ✅ Attribute modifiers for Paper 1.21 compatibility
- ✅ Full tab-completion support
- ✅ Admin commands for managing skill points

## Installation

1. Build the plugin: `mvn clean package`
2. Copy the generated JAR from `target/` to your server's `plugins/` folder
3. Restart the server
4. The plugin will automatically generate configuration files

## Commands

### `/skilltree open`
Opens the main skill tree menu

### `/skilltree info`
Displays your current skill stats and progress

### `/skilltree addpoints <player> <amount>` (Admin only)
Adds skill points to a specific player
- Requires `mineskills.admin` permission

## Configuration

The configuration file `config.yml` allows customization of:

```yaml
# Action tracking thresholds
action-tracking:
  mining:
    ore_blocks_for_point: 100
    normal_blocks_for_point: 50
  combat:
    damage_for_point: 50
  movement:
    blocks_for_point: 1000
    jumps_for_point: 50

# Auto-save interval in minutes
auto-save-interval: 5

# Starting skill points for new players
starting-skill-points: 20
```

## Permissions

- `mineskills.use` - Use skill tree commands (default: true)
- `mineskills.admin` - Add skill points to players (default: op)

## Requirements

- Minecraft Java Edition 1.21.x
- Paper 1.21.1 or later
- Java 17

## Building from Source

```bash
# Clone the repository
git clone <repository-url>
cd cto-mineskills

# Build with Maven
mvn clean package

# The JAR will be in target/mineskills-1.0.0.jar
```

## Data Storage

Player data is stored in `plugins/MineSkills/players.yml` in the following format:

```yaml
players:
  "player-uuid":
    name: "PlayerName"
    skill_points: 20
    total_points: 50
    skills:
      POWER_BLOW: 2
      IRON_SKIN: 1
      SWIFT_MOVEMENT: 3
      MINING: 1
    action_progress:
      combat: 45
      mining: 75
      movement: 320
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Support

For issues and feature requests, please use the issue tracker on GitHub.

## Credits

- Developed for Paper 1.21.x
- Uses Adventure API for modern text components
- Built with Maven
