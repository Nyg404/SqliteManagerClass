package io.github.nyg404.classWeapon;

import io.github.nyg404.classWeapon.Commads.*;
import io.github.nyg404.classWeapon.Commads.AdminCommands.ReloadPlugin;

import io.github.nyg404.classWeapon.Placeholderapi.PlayerStatsPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class ClassWeapon extends JavaPlugin {


    @Override
    public void onEnable() {


        saveDefaultConfig();

        // Регистрация событий

        // Регистрация команд
        getCommand("adminaddplayer").setExecutor(new Addplayer());
        getCommand("addxp").setExecutor(new Addxp());
        getCommand("reloadclassweapon").setExecutor(new ReloadPlugin(this));

        getLogger().info("ClassWeapon успешно загружен!");
        Plugin placeholderApi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (placeholderApi != null && placeholderApi.isEnabled()) {
            new PlayerStatsPlaceholder().register();
        } else {
            getLogger().warning("PlaceholderAPI не установлен! PlaceholderExpansion не зарегистрирован.");
        }
    }

    @Override
    public void onDisable() {
    }

}
