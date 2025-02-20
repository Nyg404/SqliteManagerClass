package io.github.nyg404.classWeapon.Sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import ru.thisistails.tailslib.Tools.YAMLManager;

public class DataManager {

    private static DataManager instance = null;

    static{
        instance = new DataManager();
    }

    public static DataManager getInstance(){
        return instance;
    }

    private DataManager(){
        connection();
    }



    static FileConfiguration file = YAMLManager.require("classundweapon", "config.yml");
    private static final String jdbc = file.getString("jdbc");
                
    // Метод для соединения с базой данных?
    public Connection connection(){
        try{
            return DriverManager.getConnection(jdbc);
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
                
    }
    public void close() {
        if (connection() != null) {
            close();
            }
    }

    public void createTabls(){

        String players = "CREATE TABLE IF NOT EXISTS players ("
                    + "uuid VARCHAR(36) PRIMARY KEY, "
                    + "player_name VARCHAR(36) NOT NULL, "
                    + "id_class INT, "
                    + "FOREIGN KEY (id_class) REFERENCES player_classes(id) ON DELETE CASCADE"
                    + ");";

        String playerStats = "CREATE TABLE IF NOT EXISTS player_stats ("
                    + "uuid VARCHAR(36) PRIMARY KEY, "
                    + "level INT, "
                    + "current_xp INT,"
                    + "required_xp INT,"
                    + "strength INT, "
                    + "FOREIGN KEY (uuid) REFERENCES players(uuid) ON DELETE CASCADE"
                    + ");";
        String playerClasses = "CREATE TABLE IF NOT EXISTS player_classes ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "class_name VARCHAR(36) NOT NULL"
                    + ");";
        

        try(Connection connection = connection(); Statement statement = connection.createStatement()){
            statement.execute(playerClasses);
            statement.execute(playerStats);
            statement.execute(players);
        }catch(SQLException exception){
            exception.printStackTrace();
        }        
    }

    public void addplayer(Player player){
        String addplayer = "INSERT INTO players(uuid, player_name, id_class) VALUES(?,?,?)";
        String addstats = "INSERT INTO player_stats(uuid, level, current_xp, required_xp, strength) VALUES(?,?,?,?,?)";
        try(Connection connection = connection(); PreparedStatement statement = connection.prepareStatement(addplayer); PreparedStatement statement1 = connection.prepareStatement(addstats) ) {
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());
            statement.setInt(3, 1);
            statement1.setString(1, player.getUniqueId().toString());
            statement1.setInt(2, 0);
            statement1.setInt(3, 0);
            statement1.setInt(4, 100);
            statement1.setInt(5, 186);
            statement.executeUpdate();
            statement1.executeUpdate();
        } catch (SQLException exception) {
            // TODO: handle exception
            exception.printStackTrace();
        }
    }

}
