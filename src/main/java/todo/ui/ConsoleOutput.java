package todo.ui;

import todo.model.Task;

import java.util.List;

public class ConsoleOutput {

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

    public void printTask(Task task){
        if (task != null)
            System.out.println(task.toDisplay());
    }

    public void printMessage(String message){
        if (message != null)
            System.out.println(message);
    }

    public void printTaskIdInputMessage(){
        System.out.println("Введите id задачи: ");
    }

    public void printTaskDescriptionInputMessage(){
        System.out.println("Введите описание задачи: ");
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
