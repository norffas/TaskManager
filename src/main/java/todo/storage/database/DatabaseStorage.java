package todo.storage.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import todo.model.Task;
import todo.model.TaskStatus;
import todo.storage.Storage;
import todo.storage.StorageException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseStorage implements Storage {
    private static final String ARGUMENTS = "databaseArguments";
    private static final Logger logger = LoggerFactory.getLogger(DatabaseStorage.class);
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS tasks " +
            "(id INT PRIMARY KEY, " +
            "description TEXT NOT NULL, " +
            "task_status VARCHAR(20) NOT NULL," +
            "created_at TIMESTAMP NOT NULL)";



    public List<Task> load(){
        List<Task> tasks = new ArrayList<>();
        try(Connection connection = connectBase()){
            try(Statement statement = connection.createStatement()) {
                statement.execute(CREATE_TABLE);
                try(ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks")){
                    while(resultSet.next()){
                        Task task = new Task(resultSet.getInt(1), resultSet.getString(2),
                                TaskStatus.valueOf(resultSet.getString(3)),
                                resultSet.getObject(4, LocalDateTime.class));
                        tasks.add(task);
                    }
                    return tasks;
                }
            }

        }
        catch (SQLException e){
            String msg = "Возникла ошибка при загрузке данных из базы данных.";
            logger.error(msg, e);
            throw new StorageException(msg, e);
        }
    }


    public void save(List<Task> tasks){
        try(Connection connection = connectBase()){
            connection.setAutoCommit(false);
            try(Statement statement = connection.createStatement()) {
                statement.execute(CREATE_TABLE);
                statement.execute("DELETE FROM tasks");
                try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO tasks" +
                        "(id, description, task_status, created_at) VALUES (?, ?, ?, ?)")){
                    for (Task task : tasks){
                        preparedStatement.setInt(1, task.getId());
                        preparedStatement.setString(2, task.getDescription());
                        preparedStatement.setString(3, task.getStatus().name());
                        preparedStatement.setObject(4, task.getCreatedAt());
                        preparedStatement.executeUpdate();
                    }
                    connection.commit();
                }
            }
            catch (SQLException e){
                connection.rollback();
                throw e;
            }
        }
        catch (SQLException e){
            String msg = "Возникла ошибка при сохрании данных в базу данных.";
            logger.error(msg, e);
            throw new StorageException(msg, e);
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
