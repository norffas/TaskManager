package todo.repository;

public class RepositoryException extends RuntimeException {
    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Exception e){
        super(message, e);
    }
}
