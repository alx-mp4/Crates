# 🧰 Crates

## ✨ Features
### Core Crates
Configurable weighted rewards and rarity tiers
- 🔑 Players open crates by spending keys
- 🎯 Weighted reward rolls with simple integer weights
- 🌈 Cosmetic rarity tiers: COMMON, UNCOMMON, RARE, EPIC, LEGENDARY
- ⏱️ Adjustable open delay (in ticks) for anticipation

### Player Experience
- 🧾 Read-only, paged **preview menu** to inspect possible rewards
- 🔔 Audio cues when opening crates and receiving keys

### Administration
- 🧰 Clear command suite for keys and config
- 🧮 View / set player keys and **give to all online** for events
- 🔁 Live reload of configuration

### Logging & Integrations
- 📱 Discord webhook integration with customizable avatar (Minotar)

---
## 🤝 Contributions, Suggestions, and Issues
Consider helping **Crates** become even more versatile and robust.

It is **highly recommended** to visit the [CONTRIBUTING](https://github.com/AleksandarHaralanov/Crates/blob/master/.github/CONTRIBUTING.md) guide for details on how to get started and where to focus your efforts.

For any issues with the plugin, or suggestions, please submit them [here](https://github.com/AleksandarHaralanov/Crates/issues).

---
## ⬇️ Download
Latest stable release of **Crates** can be found here on [GitHub](https://github.com/AleksandarHaralanov/Crates/releases/latest).<br/>

The plugin is fully open-source and transparent.<br/>
If you'd like additional peace of mind, you're welcome to scan the `.jar` file using [VirusTotal](https://www.virustotal.com/gui/home/upload).

---
## 📋 Requirements
Your server must be running one of the following software: [CB1060](https://github.com/AleksandarHaralanov/Crates/raw/refs/heads/master/libs/craftbukkit-1060.jar), [Project Poseidon](https://github.com/retromcorg/Project-Poseidon) or [UberBukkit](https://github.com/Moresteck/Project-Poseidon-Uberbukkit).

**Softdepend:** WorldGuard

---
## 🚀 Usage
By default, only OPs have permission.

Use **PermissionsEx** or similar plugins to grant groups the permission, enabling the commands.

### Commands:
| Command | Permission | Description |
|---------|------------|-------------|
| `/crates` | `crates.use` | View Crates commands. |
| `/crates reload` | `crates.config` | Reload Crates configuration. |
| `/crates delay <ticks>` | `crates.config` | Set crate open delay (20 ticks = 1s). |
| `/crates keys` | `crates.keys.view` | View your key count. |
| `/crates keys <player>` | `crates.keys.view.other` | View another player's key count. |
| `/crates keys set <player> <amount>` | `crates.keys.set` | Set a player's keys. |
| `/crates keys giveall <amount>` | `crates.keys.giveall` | Give keys to all online players. |

**Aliases:** `/crate`, `/crs`

### Permissions:
| Permission | Description |
|------------|-------------|
| `crates.*` | Wildcard permission that grants all permissions. |
| `crates.use` | Allows viewing basic Crates help/about. |
| `crates.open` | Allows players to open crates. |
| `crates.config` | Allows reloading and modifying Crates configuration (incl. delay). |
| `crates.keys.*` | Wildcard for all key-related permissions. |
| `crates.keys.view` | View your own key count. |
| `crates.keys.view.other` | View another player's key count. |
| `crates.keys.set` | Set a player's keys. |
| `crates.keys.giveall` | Give keys to all online players. |

---
## ⚙️ Configurations
Crates generates a configuration file using the default settings in the **Crates** directory.

It defines the webhook options, crate open delay, and the reward table.

#### Main Config `config.yml`:
```yaml
# ===================================================================================
# Crates Configuration
# ===================================================================================
# crate-open-delay: (Measured in ticks; 20 ticks = 1 second — 1 tick = 50 ms)
#
# Each reward needs to have:
#   id:     (String item ID, e.g., "35:3" for light blue wool)
#   amount: (How much of the item is given)
#   weight: (Relative chance — higher = more common)
#   tier:   (Reward tier: COMMON, UNCOMMON, RARE, EPIC, LEGENDARY — purely cosmetic)
# ===================================================================================

webhook:
  enabled: false
  url: "INSERT_WEBHOOK_URL"
  avatar-api: "https://minotar.net/avatar/%player%.png"

crate-open-delay: 20

rewards:
  - id: "1"
    amount: 5
    weight: 5
    tier: COMMON

  - id: "2"
    amount: 4
    weight: 4
    tier: UNCOMMON

  - id: "3"
    amount: 3
    weight: 3
    tier: RARE

  - id: "4"
    amount: 2
    weight: 2
    tier: EPIC

  - id: "5"
    amount: 1
    weight: 1
    tier: LEGENDARY
```
<br/>

> [!NOTE]  
> **Weights & Rolls:** Each reward's `weight` is added to a total pool. A random draw selects a reward proportional to its weight.  
> **Tiers are cosmetic:** `tier` only affects labeling in menus/embeds; it does not directly change probability unless you set weights accordingly.

## 📊 Project Statistics
<img src="https://repobeats.axiom.co/api/embed/d9987e6e276d665f58b96d06c76debb6a26690b9.svg" alt="Statistics" />
