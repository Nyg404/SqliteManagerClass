package io.github.nyg404.classWeapon;

import io.github.nyg404.classWeapon.Commads.*;
import io.github.nyg404.classWeapon.Commads.AdminCommands.ReloadPlugin;
import io.github.nyg404.classWeapon.Sqlite.SqliteManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ClassWeapon extends JavaPlugin {
    private SqliteManager sqliteManager;
    private static ClassWeapon instance;

    @Override
    public void onEnable() {
        instance = this;
        sqliteManager = new SqliteManager(this);
        sqliteManager.init();
        sqliteManager.createTabls();
//        ClassWeaponAPI.init(sqliteManager);
        saveDefaultConfig();

        // Регистрация событий

        // Регистрация команд
        getCommand("admincreateclass").setExecutor(new AdminCreateclass(sqliteManager));
        getCommand("adminaddplayer").setExecutor(new Addplayer(sqliteManager));
        getCommand("addxp").setExecutor(new Addxp(sqliteManager));
        getCommand("score").setExecutor(new Scored());
        getCommand("checklevel").setExecutor(new Check(sqliteManager));
        getCommand("reloadclassweapon").setExecutor(new ReloadPlugin(this));

        getLogger().info("ClassWeapon успешно загружен!");
    }

    @Override
    public void onDisable() {
        if (sqliteManager != null) {
            sqliteManager.close(); // Закрываем соединение с базой
        }
        getLogger().info("ClassWeapon выключен!");
    }

    public static ClassWeapon getInstance() {
        return instance;
    }
}
