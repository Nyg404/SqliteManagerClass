package io.github.nyg404.classWeapon.Commads;

import io.github.nyg404.classWeapon.API.PlayerStatisteck;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Addxp implements CommandExecutor {



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

        Player target = Bukkit.getPlayer(targetName);

        if(target == null){
            return false;
        }

        PlayerStatisteck playerStatisteck = new PlayerStatisteck(target);

        playerStatisteck.setCurrent_xp(playerStatisteck.getCurrent_xp() + addxp);


        return true;
    }
}
