package todo.storage;

import todo.model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {
    private static final String MY_FILE = "tasks.txt";

    public void save(List<Task> tasks)  {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(MY_FILE))){
            for(Task task : tasks){
                writer.write(task.getId() + ":|" + task.getDescription() + ":|" + task.getStatus() + ":|" + task.getCreatedAt() + "\n");
            }
        }
        catch (IOException e) {
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
        } catch (FileNotFoundException e){
            throw new StorageException("Файл для сохранения не найден", e);
        }
        catch (IOException e) {
            throw new StorageException("Ошибка потока", e);
        }

        return tasks;
    }

    private Task parseTaskFromString(String line){
        String[] string = line.split(":\\|");
        return new Task(Integer.parseInt(string[0]), string[1], string[2], string[3]);
    }

}
