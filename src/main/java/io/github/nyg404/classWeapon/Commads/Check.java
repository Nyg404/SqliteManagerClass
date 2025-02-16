package io.github.nyg404.classWeapon.Commads;

import io.github.nyg404.classWeapon.API.PlayerStatisteck;
import io.github.nyg404.classWeapon.Sqlite.SqliteManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Check implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerStatisteck playerStatisteck = new PlayerStatisteck(player);
            int level = playerStatisteck.getLevel();
            player.sendMessage(Component.text("Ваш уровень: " + level));
            return true;
        } else {
            sender.sendMessage("Команда доступна только игрокам!");
            return false;
        }
    }
}
