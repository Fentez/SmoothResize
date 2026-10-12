# SmoothResize

A lightweight Paper plugin for smooth, animated player resizing using Minecraft's native `SCALE` attribute.

**Author:** [Fentez](https://fentez.pp.ua) • **GitHub:** [Fentez/SmoothResize](https://github.com/Fentez/SmoothResize)

[English](#english) • [Українська](#українська)

---

## English

**SmoothResize** allows players and administrators to smoothly animate player model size transitions using the `SCALE` attribute introduced in modern Minecraft versions.

### Key Features

* **Smooth Scale Animation**: Gradually interpolates a player's scale over a configurable number of ticks instead of snapping instantly.
* **Tiered Scale Limits**: Separate min/max scale ranges for regular players and staff/VIPs with extended permissions.
* **Safe Disconnect Handling**: Automatically finalizes the target scale if a player disconnects or the server shuts down mid-animation.
* **MiniMessage Formatting**: Full support for Adventure MiniMessage tags, gradients, and hex colors.
* **Configurable Cooldowns**: Built-in command cooldown with bypass permission.

### Commands & Permissions

| Command | Description | Permission | Default |
| --- | --- | --- | --- |
| `/resize <scale>` | Smoothly change your own size | `smoothresize.resize` | `true` |
| `/resize <scale> <player>` | Smoothly change another player's size | `smoothresize.resize.others` | `op` |
| `/resize reload` | Reload plugin configuration | `smoothresize.reload` | `op` |
| *(Extended range)* | Use extended min/max scale limits | `smoothresize.resize.extended` | `op` |
| *(Cooldown bypass)* | Bypass resize command cooldown | `smoothresize.bypass.cooldown` | `op` |

**Aliases:** `/res`

### Configuration (`config.yml`)

```yaml
# Prefix displayed before all plugin messages (supports MiniMessage formatting)
prefix: "<light_purple>[<dark_purple>SmoothResize</dark_purple>]</light_purple> "

# Minimum and maximum scale allowed for regular players (smoothresize.resize)
default_min_scale: 0.8
default_max_scale: 1.15

# Minimum and maximum scale allowed for players with extended permission (smoothresize.resize.extended)
extended_min_scale: 0.0625
extended_max_scale: 16.0

# Cooldown time in seconds before a player can use the command again
cooldown_seconds: 30

# Number of animation steps for smooth resizing (higher = smoother and slower, 20 steps = 1 second)
resize_steps: 20

# Plugin messages (supports MiniMessage color and formatting tags)
messages:
  config_reloaded: "<green>Configuration successfully reloaded.</green>"
  player_not_found: "<red>Player <yellow>%player%</yellow> was not found.</red>"
  only_player: "<red>Only players can use this command.</red>"
  invalid_number: "<red>Invalid number format.</red>"
  usage: "<red>Usage: /resize <scale> [player]</red>"
  scale_range: "<red>Scale must be between <yellow>%min%</yellow> and <yellow>%max%</yellow>.</red>"
  cooldown: "<red>Please wait <yellow>%time%</yellow> seconds before resizing again.</red>"
  resized: "<green>Your scale has been changed to <yellow>%scale%</yellow>.</green>"
```

### Build & Installation

```bash
mvn clean package
```

1. Requires **Paper 1.21+** and **Java 21+**.
2. Place `target/SmoothResize-1.0.0.jar` into your server's `plugins/` folder.
3. Restart the server.

---

## Українська

**SmoothResize** — це легкий плагін для Paper, який дозволяє плавно змінювати розмір моделі гравця з анімацією за допомогою нативного атрибута `SCALE`.

### Основні можливості

* **Плавна анімація**: Поступово змінює розмір гравця протягом налаштованої кількості тіків без різких стрибків.
* **Дворівневі ліміти**: Окремі мінімальні та максимальні межі масштабу для звичайних гравців та адміністраторів.
* **Безпечний вихід**: Автоматично застосовує кінцевий розмір при виході гравця або вимкненні сервера під час анімації.
* **Підтримка MiniMessage**: Повна підтримка кольорів, градієнтів та форматування MiniMessage.
* **Система кулдаунів**: Налаштовувана затримка між командами з правом обходу.

### Команди та права

| Команда | Опис | Право | За замовчуванням |
| --- | --- | --- | --- |
| `/resize <scale>` | Плавно змінити власний розмір | `smoothresize.resize` | `true` |
| `/resize <scale> <player>` | Змінити розмір іншого гравця | `smoothresize.resize.others` | `op` |
| `/resize reload` | Перезавантажити конфігурацію | `smoothresize.reload` | `op` |
| *(Розширені межі)* | Доступ до розширених лімітів розміру | `smoothresize.resize.extended` | `op` |
| *(Обхід кулдауну)* | Використання команди без затримки | `smoothresize.bypass.cooldown` | `op` |

**Аліаси:** `/res`
