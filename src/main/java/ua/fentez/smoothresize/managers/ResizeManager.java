package ua.fentez.smoothresize.managers;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ua.fentez.smoothresize.SmoothResize;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ResizeManager implements Listener {
    private final SmoothResize plugin;
    private final Map<UUID, Long> lastUsage = new HashMap<>();
    private final Map<UUID, BukkitTask> tasks = new HashMap<>();
    private final Map<UUID, Double> targetScales = new HashMap<>();

    public ResizeManager(SmoothResize plugin) {
        this.plugin = plugin;
    }

    public void shutdown() {
        tasks.forEach((uuid, task) -> {
            task.cancel();
            Player player = plugin.getServer().getPlayer(uuid);
            if (player != null && player.isOnline()) {
                AttributeInstance scaleAttribute = player.getAttribute(Attribute.SCALE);
                if (scaleAttribute != null && targetScales.containsKey(uuid)) {
                    scaleAttribute.setBaseValue(targetScales.get(uuid));
                }
            }
        });
        tasks.clear();
        targetScales.clear();
        lastUsage.clear();
    }

    public boolean hasCooldown(Player player) {
        if (player.hasPermission("smoothresize.bypass.cooldown")) return false;
        long timeSince = (System.currentTimeMillis() - lastUsage.getOrDefault(player.getUniqueId(), 0L)) / 1000;
        return timeSince < plugin.getConfigManager().getCooldownSeconds();
    }

    public long getRemainingCooldown(Player player) {
        long timeSince = (System.currentTimeMillis() - lastUsage.getOrDefault(player.getUniqueId(), 0L)) / 1000;
        return Math.max(0, plugin.getConfigManager().getCooldownSeconds() - timeSince);
    }

    public void updateUsage(Player player) {
        lastUsage.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public void resize(Player player, double target) {
        UUID uuid = player.getUniqueId();
        BukkitTask existing = tasks.remove(uuid);
        if (existing != null) existing.cancel();

        AttributeInstance scaleAttribute = player.getAttribute(Attribute.SCALE);
        if (scaleAttribute == null) {
            plugin.getLogger().warning("Attribute SCALE not available for " + player.getName());
            return;
        }

        double initial = scaleAttribute.getBaseValue();
        int steps = plugin.getConfigManager().getResizeSteps();
        double step = (target - initial) / steps;

        targetScales.put(uuid, target);

        BukkitTask task = new BukkitRunnable() {
            int count = 0;
            @Override
            public void run() {
                if (++count >= steps) {
                    scaleAttribute.setBaseValue(target);
                    tasks.remove(uuid);
                    targetScales.remove(uuid);
                    cancel();
                } else {
                    scaleAttribute.setBaseValue(initial + step * count);
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
        tasks.put(uuid, task);
    }

    public boolean isValid(double scale, boolean extended) {
        double min = extended ? plugin.getConfigManager().getExtendedMinScale() : plugin.getConfigManager().getDefaultMinScale();
        double max = extended ? plugin.getConfigManager().getExtendedMaxScale() : plugin.getConfigManager().getDefaultMaxScale();
        return scale >= min && scale <= max;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        BukkitTask task = tasks.remove(uuid);
        if (task != null) {
            task.cancel();
            AttributeInstance scaleAttribute = event.getPlayer().getAttribute(Attribute.SCALE);
            if (scaleAttribute != null && targetScales.containsKey(uuid)) {
                scaleAttribute.setBaseValue(targetScales.get(uuid));
            }
            targetScales.remove(uuid);
        }
    }
}
