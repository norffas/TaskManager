package todo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {
    private final String My_File = "tasks.txt";

    public void save(List<Task> tasks){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(My_File))){
            for(Task task : tasks){
                writer.write(task.getId() + ":|" + task.getDescription() + ":|" + task.getStatus() + ":|" + task.getCreatedAt() + "\n");
            }
        }
        catch(IOException e){
            System.out.println("Ошибка потока");
        }
    }

    public List<Task> load(){
        List<Task> tasks = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(My_File))){
            String string;
            while((string = reader.readLine()) != null){
                tasks.add(parseTaskFromString(string));
            }
        }
        catch(FileNotFoundException e){
            System.out.println("Файл не найден");
        }
        catch(IOException e){
            System.out.println("Ошибка потока");
        }
        return tasks;
    }

    private Task parseTaskFromString(String line){
        String[] string = line.split(":\\|");
        return new Task(Integer.parseInt(string[0]), string[1], string[2], string[3]);
    }

}
