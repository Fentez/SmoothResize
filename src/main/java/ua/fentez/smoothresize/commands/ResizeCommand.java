package ua.fentez.smoothresize.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ua.fentez.smoothresize.SmoothResize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResizeCommand extends Command {
    private final SmoothResize plugin;

    public ResizeCommand(SmoothResize plugin) {
        super("resize");
        this.setDescription("Smoothly change a player's size.");
        this.setUsage("/resize <scale> [player] | /resize reload");
        this.setAliases(List.of("res"));
        this.setPermission("smoothresize.resize");

        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length >= 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("smoothresize.reload")) {
            plugin.getConfigManager().load();
            sender.sendMessage(plugin.getConfigManager().getMessage("config_reloaded"));
            return true;
        }

        Player target;
        double scale;

        if (args.length >= 2 && sender.hasPermission("smoothresize.resize.others")) {
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(plugin.getConfigManager().getMessage("player_not_found", "player", args[1]));
                return true;
            }
            try {
                scale = Double.parseDouble(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(plugin.getConfigManager().getMessage("invalid_number"));
                return true;
            }
        } else if (args.length == 1) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(plugin.getConfigManager().getMessage("only_player"));
                return true;
            }
            target = player;
            try {
                scale = Double.parseDouble(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(plugin.getConfigManager().getMessage("invalid_number"));
                return true;
            }
        } else {
            sender.sendMessage(plugin.getConfigManager().getMessage("usage"));
            return true;
        }

        boolean extended = sender.hasPermission("smoothresize.resize.extended");
        if (!plugin.getResizeManager().isValid(scale, extended)) {
            double min = extended ? plugin.getConfigManager().getExtendedMinScale() : plugin.getConfigManager().getDefaultMinScale();
            double max = extended ? plugin.getConfigManager().getExtendedMaxScale() : plugin.getConfigManager().getDefaultMaxScale();
            sender.sendMessage(plugin.getConfigManager().getMessage("scale_range", "min", String.valueOf(min), "max", String.valueOf(max)));
            return true;
        }

        if (sender.equals(target) && plugin.getResizeManager().hasCooldown(target)) {
            sender.sendMessage(plugin.getConfigManager().getMessage("cooldown", "time", String.valueOf(plugin.getResizeManager().getRemainingCooldown(target))));
            return true;
        }

        if (sender.equals(target)) {
            plugin.getResizeManager().updateUsage(target);
        }
        plugin.getResizeManager().resize(target, scale);

        if (sender != target) {
            sender.sendMessage(plugin.getConfigManager().getMessage("resized", "scale", String.valueOf(scale)));
        }
        target.sendMessage(plugin.getConfigManager().getMessage("resized", "scale", String.valueOf(scale)));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            if (sender.hasPermission("smoothresize.reload")) {
                completions.add("reload");
            }
            if (sender.hasPermission("smoothresize.resize.extended")) {
                completions.addAll(List.of(
                    String.valueOf(plugin.getConfigManager().getExtendedMinScale()),
                    "1.0",
                    String.valueOf(plugin.getConfigManager().getExtendedMaxScale())
                ));
            } else {
                completions.addAll(List.of(
                    String.valueOf(plugin.getConfigManager().getDefaultMinScale()),
                    "1.0",
                    String.valueOf(plugin.getConfigManager().getDefaultMaxScale())
                ));
            }
            return completions.stream().filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase())).toList();
        } else if (args.length == 2 && sender.hasPermission("smoothresize.resize.others")) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }
        return Collections.emptyList();
    }
}
