# 🧰 Crates

## ✨ Features
### Core Crates
Configurable weighted rewards and rarity tiers
- 🔑 Players open crates by spending virtual keys
- 🎯 Weighted reward rolls with simple integer weights
- 🌈 Cosmetic rarity tiers: COMMON, UNCOMMON, RARE, EPIC, and LEGENDARY
- ⏱️ Adjustable open delay (in ticks) for anticipation
- 🎞️ Adjustable toggle option for the roulette-like spin animation during opening

### Player Experience
- 🧾 Paged **preview menu** to inspect all configured rewards and their amount
- ✨ Effects when opening crates
- 🔔 Audio cues when opening crates, receiving keys, and the preview menu

### Administration
- 🧰 Clear command suite for keys and config
- 🧮 View / set player keys and **give to all online** for events
- 🔁 Live reload of configuration

### Logging & Integrations
- 📱 Discord webhook integration with customizable avatar
- 💬 In-game message congratulating you on your award; if it's EPIC or LEGENDARY, everyone will see it

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
| Command                              | Permission               | Description                                    |
|--------------------------------------|--------------------------|------------------------------------------------|
| `/crates`                            | `crates.use`             | Displays Crates help/about menu.               |
| `/crates reload`                     | `crates.config`          | Reloads the Crates configuration.              |
| `/crates delay <ticks>`              | `crates.config`          | Sets crate open delay *(20 ticks = 1 second)*. |
| `/crates keys`                       | `crates.keys.view`       | Shows your current crate key count.            |
| `/crates keys <player>`              | `crates.keys.view.other` | View another player’s crate key count.         |
| `/crates keys set <player> <amount>` | `crates.keys.set`        | Set a specific player’s key amount.            |
| `/crates keys giveall <amount>`      | `crates.keys.giveall`    | Give keys to all online players.               |


**Aliases:** `/crate`, `/crs`

### Permissions:
| Permission               | Default | Description                                     |
|--------------------------|---------|-------------------------------------------------|
| `crates.*`               | `op`    | Grants all Crates permissions.                  |
| `crates.use`             | `true`  | Allows viewing the main Crates help/about menu. |
| `crates.open`            | `true`  | Allows opening crates.                          |
| `crates.config`          | `op`    | Allows reloading config and setting open delay. |
| `crates.keys.*`          | `op`    | Grants all key-related permissions.             |
| `crates.keys.view`       | `true`  | Allows viewing your own key count.              |
| `crates.keys.view.other` | `op`    | Allows viewing another player’s key count.      |
| `crates.keys.set`        | `op`    | Allows setting a player’s key count.            |
| `crates.keys.giveall`    | `op`    | Allows giving keys to all online players.       |


---
## ⚙️ Configurations
Crates generates a configuration file using the default settings in the **Crates** directory.

It defines the webhook options, crate open delay, and the reward table.

#### Main Config `config.yml`:
```yaml
# ========================================================================
# Crates Configuration
# ------------------------------------------------------------------------
# crate-open-delay:  Delay before opening (20 ticks = 1 second)
# roulette-animation:  Toggle roulette spin effect
#
# webhook:
#   enabled:     Enable Discord webhook notifications
#   url:         Webhook endpoint (replace with your URL)
#   avatar-api:  Player avatar API (%player% = username placeholder)
#
# Each reward requires:
#   id:      Item ID (e.g. "35:3" = light blue wool)
#   amount:  Quantity of the item
#   weight:  Chance weight (higher = more common)
#   tier:    Cosmetic rarity (COMMON, UNCOMMON, RARE, EPIC, and LEGENDARY)
# ========================================================================

webhook:
  enabled: false
  url: "INSERT_WEBHOOK_URL"
  avatar-api: "https://minotar.net/avatar/%player%.png"

crate-open-delay: 20
roulette-animation: true

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
