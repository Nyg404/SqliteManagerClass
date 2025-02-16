package io.github.nyg404.classWeapon.Commads.AdminCommands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadPlugin implements CommandExecutor {

    private final JavaPlugin plugin;

    public ReloadPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("classweapon.reload")) { // Проверка на права
            plugin.getLogger().info("Перезагрузка плагина ClassWeapon...");

            // Закрываем соединения и очищаем ресурсы
            plugin.onDisable();

            // Принудительно загружаем плагин заново
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            plugin.getServer().getPluginManager().enablePlugin(plugin);

            sender.sendMessage("Плагин ClassWeapon был перезагружен.");
        } else {
            sender.sendMessage("У вас нет прав для перезагрузки плагина.");
        }
        return true;
    }
}
