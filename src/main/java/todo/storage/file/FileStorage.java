package todo.storage.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import todo.model.Task;
import todo.model.TaskStatus;
import todo.storage.Storage;
import todo.storage.StorageException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class FileStorage implements Storage {
    private static final String MY_FILE = "tasks.txt";
    private static final String TEMP_FILE = "temp.txt";
    private static final Logger logger = LoggerFactory.getLogger(FileStorage.class);

    public void save(List<Task> tasks)  {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(TEMP_FILE))){
            for(Task task : tasks){
                writer.write(task.getId() + ":|" + task.getDescription() + ":|" + task.getStatus() + ":|" + task.getCreatedAt() + "\n");
            }
        }
        catch (IOException e) {
            logger.error("При попытке сохранить задачи в {} произошла ошибка.", MY_FILE, e);
            throw new StorageException("Ошибка при сохранении", e);
        }
        try {
            Path mainFile = Path.of(MY_FILE);
            Path tempFile = Path.of(TEMP_FILE);
            Files.move(tempFile, mainFile, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            logger.error("При попытке сохранить задачи в {} произошла ошибка.", MY_FILE, e);
            throw new StorageException("Ошибка при сохранении", e);
        }
        logger.info("Данные успешно сохранились в файл {}", MY_FILE);
    }

    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(MY_FILE))){
            String string;
            while((string = reader.readLine()) != null){
                tasks.add(parseTaskFromLine(string));
            }
            logger.info("Данные из файла {} успешно загружены.", MY_FILE);
        } catch (FileNotFoundException e){
            logger.info("Файл {} для загрузки данных в работу не найден, используется пустой список задач.", MY_FILE);
            // для новых пользователей файл будет создан, не является ошибкой
        }
        catch (IOException e) {
            logger.error("Ошибка при попытке считать данные из файла {}.", MY_FILE, e);
            throw new StorageException("Ошибка потока", e);
        }
        catch (StorageException e){
            logger.error("Файл {} поврежден.", MY_FILE, e);
            throw e;
        }

        return tasks;
    }

    private Task parseTaskFromLine(String line){
        String[] taskFields = line.split(":\\|", -1);// TODO подумать о выводе в логе информации, в какой задаче битая строка
        int id;
        String description;
        TaskStatus status;
        LocalDateTime createdAt;
        if(taskFields.length != 4)
            throw new StorageException("Файл поврежден.");
        try {
            id = Integer.parseInt(taskFields[0]);
            description = taskFields[1];
            status = TaskStatus.valueOf(taskFields[2]);
            createdAt = LocalDateTime.parse(taskFields[3]);
        } catch (IllegalArgumentException | DateTimeParseException e) {
            throw new StorageException("Файл поврежден.", e);
        }
        return new Task(id, description, status, createdAt);
    }

}
