package todo.ui;

import todo.model.Task;

import java.util.List;

public class ConsoleOutput implements Output {

    public void printError(String message){
        if(message == null)
            System.out.println("Ошибка");
        else{
            System.out.println(message);
        }
    }

    public void printMenu(){
        for(Menu value : Menu.values()){
            System.out.println(value.getCode() + ". " + value.getDisplayName());
        }
    }

    public void prompt(String string){
        System.out.print(string);
    }

    public void printTask(Task task){
        if (task != null)
            System.out.println(task.toDisplay());
    }

    public void printMessage(String message){
        if (message != null)
            System.out.println(message);
    }

    public void printTasks(List<Task> tasks){
        if(tasks != null){
            if(tasks.isEmpty())
                System.out.println("Список задач пуст");
            else {
                for (Task task : tasks)
                    System.out.println(task.toDisplay());
            }
        }
    }

}
