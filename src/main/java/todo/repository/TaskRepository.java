package todo.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import todo.model.Task;
import todo.model.TaskStatus;
import todo.storage.StorageException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository implements Repository {
    private static final Logger logger = LoggerFactory.getLogger(TaskRepository.class);
    private static final String ARGUMENTS = "databaseArguments";

    @Override
    public Task saveTask(String description){
        Task task = new Task(-1, description);
        try(Connection connection = connectBase()){
            try(PreparedStatement preparedStatement = connection.prepareStatement
                    ("INSERT INTO tasks(description, task_status, created_at) Values(?, ?, ?) Returning id")) {
                preparedStatement.setString(1, task.getDescription());
                preparedStatement.setString(2, task.getStatus().name());
                preparedStatement.setObject(3, task.getCreatedAt());
                try (ResultSet returnData = preparedStatement.executeQuery()){
                    if(returnData.next())
                        task = new Task(returnData.getInt(1), task.getDescription(), task.getStatus(), task.getCreatedAt());
                    else
                        throw new RuntimeException();
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return task;
    }


    @Override
    public Task findTaskById(int id){
        try(Connection connection = connectBase()){
            try(PreparedStatement preparedStatement = connection.prepareStatement
                    ("SELECT * FROM tasks WHERE id = ?")){
                preparedStatement.setInt(1, id);
                try(ResultSet returnData = preparedStatement.executeQuery()){
                    if(returnData.next()){
                        return new Task(id, returnData.getString(2),
                                TaskStatus.valueOf(returnData.getString(3)),
                                returnData.getObject(4, LocalDateTime.class));
                    }
                    else{
                        throw new RuntimeException();
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Task deleteTaskById(int id) {
        try(Connection connection = connectBase()){
            try(PreparedStatement preparedStatement = connection.prepareStatement
                    ("DELETE FROM tasks WHERE id = ? Returning *")){
                preparedStatement.setInt(1, id);
                try(ResultSet returnData = preparedStatement.executeQuery()){
                    if(returnData.next()){
                        return new Task(id, returnData.getString(2),
                                TaskStatus.valueOf(returnData.getString(3)),
                                returnData.getObject(4, LocalDateTime.class));
                    }
                    else
                        throw new RuntimeException();
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Task update(int id, TaskStatus status){
        try(Connection connection = connectBase()){
            try(PreparedStatement preparedStatement = connection.prepareStatement
                    ("UPDATE tasks SET task_status = ? WHERE id = ? RETURNING description, created_at")){
                preparedStatement.setString(1, String.valueOf(status));
                preparedStatement.setInt(2, id);
                try(ResultSet returnData = preparedStatement.executeQuery()){
                   if(returnData.next()){
                       return new Task(id, returnData.getString(1), status, returnData.getObject(2, LocalDateTime.class));
                   }
                   else
                       throw new RuntimeException();
               }
            }
        }
        catch(SQLException e){
            throw new RuntimeException();
        }
    }

    @Override
    public List<Task> findAll(){
        List<Task> tasks = new ArrayList<>();
        try(Connection connection = connectBase()){
            try(PreparedStatement preparedStatement = connection.prepareStatement
                    ("SELECT * FROM tasks")){
                ResultSet returnData = preparedStatement.executeQuery();
                while(returnData.next()){
                    tasks.add(new Task(returnData.getInt(1),
                            returnData.getString(2),
                            TaskStatus.valueOf(returnData.getString(3)),
                            returnData.getObject(4, LocalDateTime.class)));
                }
            }
        }
        catch(SQLException e){
            throw new RuntimeException();
        }
        return tasks;
    }

    public void createTable() throws SQLException {
        Connection connection = connectBase();
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS tasks " +
                "(id INT PRIMARY KEY, " +
                "description TEXT NOT NULL, " +
                "task_status VARCHAR(20) NOT NULL," +
                "created_at TIMESTAMP NOT NULL)");
        statement.close();
        connection.close();
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
