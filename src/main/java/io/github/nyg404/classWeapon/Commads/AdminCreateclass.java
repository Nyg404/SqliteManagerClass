package io.github.nyg404.classWeapon.Commads;

import io.github.nyg404.classWeapon.Sqlite.SqliteManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class AdminCreateclass implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Использование: /admincreateclass <название класса>");
            return false;
        }

        String nameclass = args[0];

        if (SqliteManager.getInstance().newclass(nameclass)) {
            sender.sendMessage(ChatColor.GREEN + "Класс " + nameclass + " успешно создан!");
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Ошибка создания класса.");
            return false;
        }
    }
}
