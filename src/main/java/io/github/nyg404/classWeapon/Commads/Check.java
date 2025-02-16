package io.github.nyg404.classWeapon.Commads;

import io.github.nyg404.classWeapon.Sqlite.SqliteManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Check implements CommandExecutor {
    private final SqliteManager sqliteManager;

    public Check(SqliteManager sqliteManager) {
        this.sqliteManager = sqliteManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            int level = sqliteManager.check_level(player);
            player.sendMessage(Component.text("Ваш уровень: " + level));
            return true;
        } else {
            sender.sendMessage("Команда доступна только игрокам!");
            return false;
        }
    }
}
