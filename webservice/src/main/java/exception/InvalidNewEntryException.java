package exception;

/**
 * @author Victor Gambier
 *
 */
public class InvalidNewEntryException extends Exception {

    private static final long serialVersionUID = -3063108881597278399L;

    public InvalidNewEntryException() {
        super();
    }

    public InvalidNewEntryException(String message) {
        super(message);
    }

    public InvalidNewEntryException(Throwable cause) {
        super(cause);
    }

    public InvalidNewEntryException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidNewEntryException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
