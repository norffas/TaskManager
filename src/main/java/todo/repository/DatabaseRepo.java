package todo.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import todo.model.Task;
import todo.model.TaskStatus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseRepo implements Repository {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseRepo.class);
    private static final String ARGUMENTS = "databaseArguments";

    @Override
    public Task saveTask(Task task){
        try(Connection connection = connectBase()){
            try(PreparedStatement preparedStatement = connection.prepareStatement
                    ("INSERT INTO tasks(description, task_status, created_at) Values(?, ?, ?) Returning id")) {
                preparedStatement.setString(1, task.getDescription());
                preparedStatement.setString(2, task.getStatus().name());
                preparedStatement.setObject(3, task.getCreatedAt());
                try (ResultSet returnData = preparedStatement.executeQuery()){
                    if(returnData.next()){
                        task = new Task(returnData.getInt(1), task.getDescription(), task.getStatus(), task.getCreatedAt());
                        logger.info("Задача добавлена в базу данных.");
                    }
                    else{
                        logger.warn("Не удалось добавить задачу при корректных входных данных в базу данных.");
                        throw new RepositoryException("Не удалось добавить задачу.");
                    }

                }
            }
        }
        catch (SQLException e) {
            logger.error("Не удалось подключиться к базе данных.", e);
            throw new RepositoryException("Не удалось подключиться к базе данных.", e);
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
                        return null;
                    }
                }
            }
        }
        catch (SQLException e) {
            logger.error("Не удалось подключиться к базе данных.", e);
            throw new RepositoryException("Не удалось подключиться к базе данных.", e);
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
                        return null;
                }
            }
        }
        catch (SQLException e) {
            logger.error("Не удалось подключиться к базе данных.", e);
            throw new RepositoryException("Не удалось подключиться к базе данных.", e);
        }
    }

    @Override
    public Task update(int id, TaskStatus status){
        try(Connection connection = connectBase()){
            try(PreparedStatement preparedStatement = connection.prepareStatement
                    ("UPDATE tasks SET task_status = ? WHERE id = ? RETURNING description, created_at")){
                preparedStatement.setString(1, status.name());
                preparedStatement.setInt(2, id);
                try(ResultSet returnData = preparedStatement.executeQuery()){
                   if(returnData.next()){
                       return new Task(id, returnData.getString(1), status, returnData.getObject(2, LocalDateTime.class));
                   }
                   else
                       return null;
               }
            }
        }
        catch(SQLException e){
            logger.error("Не удалось подключиться к базе данных.", e);
            throw new RepositoryException("Не удалось подключиться к базе данных.", e);
        }
    }

    @Override
    public List<Task> findAll(){
        List<Task> tasks = new ArrayList<>();
        try(Connection connection = connectBase()){
            try(PreparedStatement preparedStatement = connection.prepareStatement
                    ("SELECT * FROM tasks")){
                try(ResultSet returnData = preparedStatement.executeQuery()){
                    while(returnData.next()){
                        tasks.add(new Task(returnData.getInt(1),
                                returnData.getString(2),
                                TaskStatus.valueOf(returnData.getString(3)),
                                returnData.getObject(4, LocalDateTime.class)));
                    }
                }
            }
        }
        catch(SQLException e){
            logger.error("Не удалось подключиться к базе данных.", e);
            throw new RepositoryException("Не удалось подключиться к базе данных.", e);
        }
        return tasks;
    }

    @Override
    public List<Task> findTasksByStatus(TaskStatus status){
        List<Task> tasks = new ArrayList<>();
        try(Connection connection = connectBase()){
            try(PreparedStatement preparedStatement = connection.prepareStatement
                    ("SELECT * FROM tasks WHERE task_status = ?")){
                preparedStatement.setString(1, status.name());
                try(ResultSet returnData = preparedStatement.executeQuery()){
                    while(returnData.next()){
                        tasks.add(new Task(returnData.getInt(1),
                          returnData.getString(2),
                          TaskStatus.valueOf(returnData.getString(3)),
                          returnData.getObject(4, LocalDateTime.class)));
                    }
                }
            }
        }
        catch(SQLException e){
            logger.error("Не удалось подключиться к базе данных.", e);
            throw new RepositoryException("Не удалось подключиться к базе данных.", e);
        }
        return tasks;
    }

    @Override
    public int statusAutoUpdate(LocalDateTime date){
        try(Connection connection = connectBase()){
            try(PreparedStatement preparedStatement = connection.prepareStatement
                    ("UPDATE tasks SET task_status = ? WHERE task_status = ? AND created_at <= ?")){
                preparedStatement.setString(1, TaskStatus.ABANDONED.name());
                preparedStatement.setString(2, TaskStatus.PENDING.name());
                preparedStatement.setObject(3, date);
                return preparedStatement.executeUpdate();

            }
        }
        catch(SQLException e){
            logger.error("Не удалось подключиться к базе данных.", e);
            throw new RepositoryException("Не удалось подключиться к базе данных.", e);
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
            throw new RepositoryException(msg, e);
        }
        catch (SQLException e) {
            String msg = "Не удалось подключиться к базе данных.";
            logger.error(msg, e);
            throw new RepositoryException(msg, e);
        }
    }


}
