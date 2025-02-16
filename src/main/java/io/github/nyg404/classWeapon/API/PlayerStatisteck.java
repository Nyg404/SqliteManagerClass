package io.github.nyg404.classWeapon.API;

import io.github.nyg404.classWeapon.Sqlite.SqliteManager;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class PlayerStatisteck {
    private @Getter Player player;
    private @Getter boolean valid = false;
    private @Getter int level, current_xp, required_xp;
    private SqliteManager sqliteManager = SqliteManager.getInstance();
    public PlayerStatisteck(Player player) {
        this.player = player;

        try {
            PreparedStatement preparedStatement = sqliteManager.getConnection().prepareStatement("SELECT * FROM player_stats WHERE uuid = ?");
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.first()){
                sqliteManager.addplayer(player);
            }
            level = resultSet.getInt("level");
            current_xp = resultSet.getInt("current_xp");
            required_xp = resultSet.getInt("required_xp");
            valid = true;



        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void setLevel(int level) {

        if(level >= 100){
            return;
        } else {
            setRequired_xp((int) Math.ceil(required_xp * 1.01));
        }

        this.level = level;

        try {
            PreparedStatement preparedStatement = sqliteManager.getConnection().prepareStatement("UPDATE player_stats SET level = ? WHERE uuid = ?");
            preparedStatement.setInt(1, level);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCurrent_xp(int current_xp) {

        if(level >= 100){
            return;
        } else {
            setRequired_xp((int) Math.ceil(required_xp * 1.01));
        }

        this.current_xp = current_xp;
        try {
            PreparedStatement preparedStatement = sqliteManager.getConnection().prepareStatement("UPDATE player_stats SET current_xp = ? WHERE uuid = ?");


            preparedStatement.setInt(1, current_xp);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setRequired_xp(int required_xp) {
        this.required_xp = required_xp;
        try {
            PreparedStatement preparedStatement = sqliteManager.getConnection().prepareStatement("UPDATE player_stats SET required_xp = ? WHERE uuid = ?");
            preparedStatement.setInt(1, required_xp);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
