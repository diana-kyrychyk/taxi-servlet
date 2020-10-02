package ua.com.taxi.exception;

public class RollbackException extends RuntimeException {

    public RollbackException(String message, Throwable cause) {
        super(message, cause);
    }
}
