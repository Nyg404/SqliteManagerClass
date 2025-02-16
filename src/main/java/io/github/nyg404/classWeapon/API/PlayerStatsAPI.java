package io.github.nyg404.classWeapon.API;

import io.github.nyg404.classWeapon.Sqlite.SqliteManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerStatsAPI {
    private SqliteManager initialization;

    public PlayerStatsAPI(SqliteManager initialization){
        this.initialization = initialization;
    }

    public void newclass(String name){
        initialization.newclass(name);
    }

    public void addplayer(Player player){
        initialization.addplayer(player);
    }

    public int return_current_xp(Player player){
        return initialization.return_current_xp(player);
    }

    public int return_required_xp(Player player){
        return initialization.return_required_xp(player);
    }

    public int return_level(Player player){
        return initialization.return_level(player);
    }

    public String return_class_player(Player player){
        return initialization.return_class(player);
    }

    public int check_level(Player player){
        return initialization.return_level(player);
    }

    public void addXpAndCheckLevel(Player player, int addXp){
        initialization.addXpAndCheckLevel(player, addXp);
    }
}
