package todo;

public interface Command <T> {
    T execute();
}
