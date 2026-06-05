package todo.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import todo.storage.StorageException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseFactory {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseFactory.class);
    private static final String ARGUMENTS = "databaseArguments";

    public void createTable() {
        try(Connection connection = connectBase()){
            try(Statement statement = connection.createStatement()){
                statement.execute("CREATE TABLE IF NOT EXISTS tasks " +
                        "(id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                        "description TEXT NOT NULL, " +
                        "task_status VARCHAR(20) NOT NULL," +
                        "created_at TIMESTAMP NOT NULL)");
            }
        }
        catch (SQLException e){
            throw new RuntimeException();
        }
    }

    private Connection connectBase(){
        try(BufferedReader reader = new BufferedReader(new FileReader(ARGUMENTS))) {
            String url = reader.readLine();
            String username = reader.readLine();
            String password = reader.readLine();
            Connection connect = DriverManager.getConnection(url, username, password);
            logger.info("Подключение к базе данных выполнено.");
            return connect;
        }
        catch (IOException e){
            String msg = "Не удалось получить данные для входа в базу данных.";
            logger.error(msg, e);
            throw new StorageException(msg, e);
        }
        catch (SQLException e) {
            String msg = "Не удалось подключиться к базе данных.";
            logger.error(msg, e);
            throw new StorageException(msg, e);
        }
    }
}
