package io.github.nyg404.classWeapon.Commads;

import io.github.nyg404.classWeapon.Sqlite.SqliteManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class Addplayer implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Вы не указали игрока!");
            return false;
        }

        String targetName = args[0].toLowerCase();
        List<Player> matchedPlayers = Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getName().toLowerCase().startsWith(targetName))
                .collect(Collectors.toList());

        if (matchedPlayers.isEmpty()) {
            sender.sendMessage("Игрок не найден!");
            return false;
        }

        for (Player matched : matchedPlayers) {
            SqliteManager.getInstance().addplayer(matched);
            sender.sendMessage("Игрок " + matched.getName() + " добавлен в базу данных.");
        }

        return true;
    }
}
