package io.github.nyg404.classWeapon.Sqlite;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;

public class SqliteManager {
    

    private HikariDataSource dataSource;
    private final JavaPlugin plugin;

    public SqliteManager(JavaPlugin plugin){
        this.plugin = plugin;
        init();
    }


    public void init(){
        FileConfiguration configuration = plugin.getConfig();
        String dbType = configuration.getString("database.type", "sqlite");

        HikariConfig hikariConfig = new HikariConfig();

        if ("mysql".equalsIgnoreCase(dbType)){
            String host = configuration.getString("database.mysql.host");
            int port = configuration.getInt("database.mysql.port");
            String database = configuration.getString("database.mysql.database");
            String user = configuration.getString("database.mysql.user");
            String password = configuration.getString("database.mysql.password");

            hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
            hikariConfig.setUsername(user);
            hikariConfig.setPassword(password);

            hikariConfig.setMaximumPoolSize(10);
            hikariConfig.setMinimumIdle(2);
            hikariConfig.setIdleTimeout(30000);
            hikariConfig.setMaxLifetime(1800000);
            hikariConfig.setConnectionTimeout(10000);
            hikariConfig.setLeakDetectionThreshold(2000);
            hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");

            this.dataSource = new HikariDataSource(hikariConfig);

        } else {
            String dbFile = plugin.getDataFolder() + "/" + configuration.getString("database.sqlite.file");
            hikariConfig.setJdbcUrl("jdbc:sqlite:" + dbFile);
            hikariConfig.setDriverClassName("org.sqlite.JDBC");
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        if (this.dataSource != null) {
            this.dataSource.close();
        }
    }

    public void createTabls() {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {

            // Создание таблицы player_classes
            String playerClasses = "CREATE TABLE IF NOT EXISTS player_classes ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "class_name VARCHAR(36) NOT NULL"
                    + ");";
            statement.execute(playerClasses);

            String players = "CREATE TABLE IF NOT EXISTS players ("
                    + "uuid VARCHAR(36) PRIMARY KEY, "
                    + "player_name VARCHAR(36) NOT NULL, "
                    + "id_class INT, "
                    + "FOREIGN KEY (id_class) REFERENCES player_classes(id) ON DELETE CASCADE"
                    + ");";
            statement.execute(players);

            String playerStats = "CREATE TABLE IF NOT EXISTS player_stats ("
                    + "uuid VARCHAR(36) PRIMARY KEY, "
                    + "level INT, "
                    + "current_xp INT,"
                    + "required_xp INT,"
                    + "strength INT, "
                    + "FOREIGN KEY (uuid) REFERENCES players(uuid) ON DELETE CASCADE"
                    + ");";
            statement.execute(playerStats);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean newclass(String nameClass){
        String newclass = "INSERT INTO player_classes(class_name) VALUES(?)";
        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(newclass)) {
            statement.setInt(1, 0);
            statement.setString(2, nameClass);
            statement.executeUpdate();
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public void addplayer(Player player){
        String addplayers = "INSERT INTO players(uuid, player_name, id_class) VALUES(?,?,?)";
        String addstats = "INSERT INTO player_stats(uuid, level, strength, current_xp, required_xp) VALUES(?,?,?,?,?)";
        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(addplayers); PreparedStatement twostatement = connection.prepareStatement(addstats))  {
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());
            statement.setInt(3, 1);
            twostatement.setString(1, player.getUniqueId().toString());
            twostatement.setInt(2,0);
            twostatement.setInt(3, 100);
            twostatement.setInt(4, 0);
            twostatement.setInt(5, 100);
            statement.executeUpdate();
            twostatement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }


    public int return_current_xp(Player player){
        String return_current_xp = "SELECT current_xp FROM player_stats WHERE uuid = ?";
        int current_xp = 0;
        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(return_current_xp)) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                current_xp = resultSet.getInt("current_xp");
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return current_xp;
    }

    public int return_required_xp(Player player){
        String return_required_xp = "SELECT required_xp FROM player_stats WHERE uuid = ?";
        int required_xp = 0;
        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(return_required_xp)) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                required_xp = resultSet.getInt("required_xp");
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return required_xp;
    }

    public int return_level(Player player){
        String return_level = "SELECT level FROM player_stats WHERE uuid = ?";
        int level = 0;
        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(return_level)) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                level = resultSet.getInt("level");
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return level;
    }


    public int check_level(Player player) {
        int current_xp = return_current_xp(player); // Получаем текущий опыт
        int required_xp = return_required_xp(player); // Получаем требуемый опыт для следующего уровня
        int level = return_level(player); // Получаем текущий уровень игрока

        // Проверяем, достиг ли игрок максимального уровня
        if (level >= 100) {
            return level; // Если уровень 100 или выше, ничего не делаем
        }

        // Пока опыта хватает для повышения хотя бы одного уровня
        while (current_xp >= required_xp && level < 100) {
            current_xp -= required_xp; // Уменьшаем опыт на количество, потраченное на уровень
            level++; // Повышаем уровень

            required_xp = (int) Math.ceil(required_xp * 1.01); // Увеличиваем требуемый опыт для следующего уровня

            // Если достигли максимального уровня, сбрасываем лишний опыт
        }

        if (level >= 100) {
            level = 100;
            current_xp = 0; // Чтобы не было накопленного опыта выше лимита
        }

        // Обновляем данные в базе
        String updateQuery = "UPDATE player_stats SET level = ?, current_xp = ?, required_xp = ? WHERE uuid = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setInt(1, level);
            statement.setInt(2, current_xp);
            statement.setInt(3, required_xp);
            statement.setString(4, player.getUniqueId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return level;
    }




    public void add_current_xp(Player player, int addxp) {
        String add_current_xp = "UPDATE player_stats SET current_xp = current_xp + ? WHERE uuid = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(add_current_xp)) {

            // Получаем текущее значение опыта игрока из базы данных
            int current_xp = return_current_xp(player);

            // Добавляем новый опыт
            int new_current_xp = current_xp + addxp;

            // Устанавливаем параметры для SQL запроса
            statement.setInt(1, addxp);  // Добавляем новый опыт, а не текущее значение
            statement.setString(2, player.getUniqueId().toString());

            // Выполняем обновление
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addXpAndCheckLevel(Player player, int addXp) {
        String query = "SELECT current_xp, required_xp, level FROM player_stats WHERE uuid = ?";
        String updateQuery = "UPDATE player_stats SET current_xp = ?, level = ?, required_xp = ? WHERE uuid = ?";

        try (Connection connection = getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(query);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            // Получаем текущие значения
            selectStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int currentXp = resultSet.getInt("current_xp") + addXp;
                int requiredXp = resultSet.getInt("required_xp");
                int level = resultSet.getInt("level");

                // Проверяем, можно ли повысить уровень
                while (currentXp >= requiredXp && level < 100) {

                    level++;
                    requiredXp = (int) Math.ceil(requiredXp * 1.5);
                }

                // Обновляем данные в БД
                updateStatement.setInt(1, currentXp);
                updateStatement.setInt(2, level);
                updateStatement.setInt(3, requiredXp);
                updateStatement.setString(4, player.getUniqueId().toString());
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
