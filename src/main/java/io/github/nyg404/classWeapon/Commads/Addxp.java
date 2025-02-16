package io.github.nyg404.classWeapon.Commads;

import io.github.nyg404.classWeapon.Sqlite.SqliteManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class Addxp implements CommandExecutor {
    private final SqliteManager sqliteManager;

    public Addxp(SqliteManager sqliteManager) {
        this.sqliteManager = sqliteManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Использование: /addxp <игрок> <количество XP>");
            return false;
        }

        String targetName = args[0].toLowerCase();
        int addxp;

        try {
            addxp = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("XP должно быть числом!");
            return false;
        }

        List<Player> matchedPlayers = Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getName().toLowerCase().startsWith(targetName))
                .collect(Collectors.toList());

        if (matchedPlayers.isEmpty()) {
            sender.sendMessage("Игрок не найден!");
            return false;
        }

        for (Player matched : matchedPlayers) {
            int oldxp = sqliteManager.return_current_xp(matched);
            sqliteManager.addXpAndCheckLevel(matched, addxp);
            int newxp = sqliteManager.return_current_xp(matched);

            sender.sendMessage(Component.text("Игроку " + matched.getName() + " добавлено " + addxp + " XP. Теперь у него " + newxp + " XP."));
        }

        return true;
    }
}
