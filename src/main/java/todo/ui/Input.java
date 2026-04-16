package todo.ui;

public interface Input {
    String readNonEmptyLine();
    int readInt();
    void closeInput();
}
