package ua.fentez.smoothresize;

import org.bukkit.plugin.java.JavaPlugin;
import ua.fentez.smoothresize.commands.ResizeCommand;
import ua.fentez.smoothresize.config.ConfigManager;
import ua.fentez.smoothresize.managers.ResizeManager;

public final class SmoothResize extends JavaPlugin {
    private ConfigManager configManager;
    private ResizeManager resizeManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        resizeManager = new ResizeManager(this);

        configManager.load();

        getServer().getCommandMap().register("smoothresize", new ResizeCommand(this));
        getServer().getPluginManager().registerEvents(resizeManager, this);
    }

    @Override
    public void onDisable() {
        if (resizeManager != null) {
            resizeManager.shutdown();
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ResizeManager getResizeManager() {
        return resizeManager;
    }
}
