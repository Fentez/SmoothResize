package ua.fentez.smoothresize.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.file.FileConfiguration;
import ua.fentez.smoothresize.SmoothResize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    private final SmoothResize plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final Map<String, String> messages = new HashMap<>();

    private double defaultMinScale;
    private double defaultMaxScale;
    private double extendedMinScale;
    private double extendedMaxScale;
    private int cooldownSeconds;
    private int resizeSteps;
    private String prefix;

    public ConfigManager(SmoothResize plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        defaultMinScale = config.getDouble("default_min_scale", 0.8);
        defaultMaxScale = config.getDouble("default_max_scale", 1.15);
        extendedMinScale = config.getDouble("extended_min_scale", 0.0625);
        extendedMaxScale = config.getDouble("extended_max_scale", 16.0);
        cooldownSeconds = config.getInt("cooldown_seconds", 30);
        resizeSteps = config.getInt("resize_steps", 20);
        prefix = config.getString("prefix", "<light_purple>[<dark_purple>SmoothResize</dark_purple>]</light_purple> ");

        messages.clear();
        if (config.contains("messages")) {
            for (String key : config.getConfigurationSection("messages").getKeys(false)) {
                messages.put(key, config.getString("messages." + key));
            }
        }
    }

    public Component getMessage(String key, String... placeholders) {
        String message = messages.getOrDefault(key, "<red>Message not found: " + key + "</red>");
        List<TagResolver> resolvers = new ArrayList<>();
        for (int i = 0; i < placeholders.length; i += 2) {
            if (i + 1 < placeholders.length) {
                resolvers.add(Placeholder.unparsed(placeholders[i], placeholders[i + 1]));
                message = message.replace("%" + placeholders[i] + "%", "<" + placeholders[i] + ">");
            }
        }
        return miniMessage.deserialize(prefix + message, TagResolver.resolver(resolvers));
    }

    public double getDefaultMinScale() { return defaultMinScale; }
    public double getDefaultMaxScale() { return defaultMaxScale; }
    public double getExtendedMinScale() { return extendedMinScale; }
    public double getExtendedMaxScale() { return extendedMaxScale; }
    public int getCooldownSeconds() { return cooldownSeconds; }
    public int getResizeSteps() { return resizeSteps; }
}
