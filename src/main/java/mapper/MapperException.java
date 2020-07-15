package mapper;

// TODO: refactor Exception handling with best practices

/**
 * @author Victor Gambier
 *
 */
public class MapperException extends Exception {

    private static final long serialVersionUID = 0x3ABBE2L;

    public MapperException() {
    }

    public MapperException(String message) {
        super(message);
    }

    public MapperException(Throwable cause) {
        super(cause);
    }

    public MapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapperException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
