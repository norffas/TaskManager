package todo.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import todo.model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorage implements Storage {
    private static final String MY_FILE = "tasks.txt";
    private static final Logger logger = LoggerFactory.getLogger(FileStorage.class);

    public void save(List<Task> tasks)  {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(MY_FILE))){
            for(Task task : tasks){
                writer.write(task.getId() + ":|" + task.getDescription() + ":|" + task.getStatus() + ":|" + task.getCreatedAt() + "\n");
            }
            logger.info("Данные успешно сохранились в файл {}", MY_FILE);
        }
        catch (IOException e) {
            logger.error("При попытке сохранить задачи в {} произошла ошибка.", MY_FILE, e);
            throw new StorageException("Ошибка при сохранении", e);
        }

    }

    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(MY_FILE))){
            String string;
            while((string = reader.readLine()) != null){
                tasks.add(parseTaskFromString(string));
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

        return tasks;
    }

    private Task parseTaskFromString(String line){
        String[] string = line.split(":\\|"); // TODO подумать о ситуациях, когда битые строки вместо ожидаемых
        return new Task(Integer.parseInt(string[0]), string[1], string[2], string[3]);
    }

}
