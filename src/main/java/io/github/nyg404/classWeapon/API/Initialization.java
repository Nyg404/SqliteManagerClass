package io.github.nyg404.classWeapon.API;

import io.github.nyg404.classWeapon.Sqlite.SqliteManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Initialization {
    private SqliteManager initialization;

    public Initialization(SqliteManager initialization) {
        this.initialization = initialization;
    }

    public void Initialization(){
        initialization.init();
    }


    public void close(){
        initialization.close();
    }
}
