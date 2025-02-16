package io.github.nyg404.classWeapon;

import io.github.nyg404.classWeapon.Sqlite.SqliteManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;

public class Scoreboard {
    private static SqliteManager sqliteManager;

    public Scoreboard(JavaPlugin plugin){
        sqliteManager = new SqliteManager(plugin);
    }

    public static void createScoreboard(Player player) {




        // Получаем менеджер для scoreboard
        ScoreboardManager manager = Bukkit.getScoreboardManager();

        // Получаем новый Scoreboard от Bukkit API
        org.bukkit.scoreboard.Scoreboard scoreboard = manager.getNewScoreboard();

        // Создаем объект для отображения
        Objective objective = scoreboard.registerNewObjective("classStats", "dummy", "Player Stats");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Добавляем данные на scoreboard
        objective.getScore("левел:").setScore(sqliteManager.return_level(player));
        objective.getScore("Текущий опыт:").setScore(sqliteManager.return_current_xp(player));
        objective.getScore("На след уровень:").setScore(sqliteManager.return_required_xp(player));

        // Устанавливаем scoreboard для игрока
        player.setScoreboard(scoreboard);
    }
}
