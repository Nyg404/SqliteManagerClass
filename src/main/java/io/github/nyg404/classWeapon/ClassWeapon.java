package io.github.nyg404.classWeapon;

import io.github.nyg404.classWeapon.Commads.*;
import io.github.nyg404.classWeapon.Commads.AdminCommands.ReloadPlugin;
import io.github.nyg404.classWeapon.Sqlite.SqliteManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ClassWeapon extends JavaPlugin {


    @Override
    public void onEnable() {


        saveDefaultConfig();

        // Регистрация событий

        // Регистрация команд
        getCommand("admincreateclass").setExecutor(new AdminCreateclass());
        getCommand("adminaddplayer").setExecutor(new Addplayer());
        getCommand("addxp").setExecutor(new Addxp());
        getCommand("checklevel").setExecutor(new Check());
        getCommand("reloadclassweapon").setExecutor(new ReloadPlugin(this));

        getLogger().info("ClassWeapon успешно загружен!");
    }

    @Override
    public void onDisable() {
        if (SqliteManager.getInstance() != null) {
            SqliteManager.getInstance().close(); // Закрываем соединение с базой
        }
        getLogger().info("ClassWeapon выключен!");
    }

}
