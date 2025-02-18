package io.github.nyg404.classWeapon.API;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import io.github.nyg404.classWeapon.Sqlite.DataManager;
import lombok.Getter;

public class PlayerStats {

    private @Getter Player player;

    private @Getter int level;

    private @Getter int requiredXp;

    private @Getter int currentXp;

    private DataManager dataManager = DataManager.getInstance();

    public PlayerStats(Player player) {
        this.player = player;
        String query = "SELECT * FROM player_stats WHERE uuid = ?";
    
        // Используем try-with-resources, чтобы автоматически закрыть ресурсы
        try (Connection connection = dataManager.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setString(1, player.getUniqueId().toString());
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) { // проверяем, что данные были найдены
                    level = resultSet.getInt("level");
                    currentXp = resultSet.getInt("current_xp");
                    requiredXp = resultSet.getInt("required_xp");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    
    public void setLevel(int level) {
        if(level >= 100){
            return;
        } else {
            setRequiredXp((int) Math.ceil(requiredXp * 1.01));
        }

        this.level = level;

        try {
            PreparedStatement preparedStatement = dataManager.connection().prepareStatement("UPDATE player_stats SET level = ? WHERE uuid = ?");
            preparedStatement.setInt(1, level);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentXp(int currentXp, boolean showMessage) {
        if (level >= 100) {
            return; // Если уровень 100 или выше, не увеличиваем XP
        }
        int oldlevel = this.level;
        this.currentXp += currentXp;

        // Повышаем уровень, пока текущий опыт не меньше требуемого
        int levelsGained = 0;
        while (this.currentXp >= requiredXp && level < 100) {
            levelsGained++;
            this.level++; // Повышаем уровень

            // Увеличиваем требуемый опыт на 10%
            if (this.level < 100) {
                this.requiredXp = (int) Math.ceil(this.requiredXp * 1.10);

            }
        }

        if(showMessage){
            player.sendTitle("Поздравляем вас с повышением уровня! ", " Был: " + oldlevel + " Стал: "+ level);
        }

        // Если уровень был повышен, обновляем в базе данных
        if (levelsGained > 0) {
            try (PreparedStatement preparedStatement = dataManager.connection().prepareStatement("UPDATE player_stats SET current_xp = ?, level = ?, required_xp = ? WHERE uuid = ?")) {
                preparedStatement.setInt(1, this.currentXp);
                preparedStatement.setInt(2, this.level);
                preparedStatement.setInt(3, this.requiredXp);
                preparedStatement.setString(4, player.getUniqueId().toString());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void setRequiredXp(int requiredXp) {
        this.requiredXp = requiredXp;
        try {
            PreparedStatement preparedStatement = dataManager.connection().prepareStatement("UPDATE player_stats SET required_xp = ? WHERE uuid = ?");
            preparedStatement.setInt(1, requiredXp);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    
    

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    
}
