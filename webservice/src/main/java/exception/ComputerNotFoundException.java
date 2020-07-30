package exception;

/**
 * @author Victor Gambier
 *
 */
public class ComputerNotFoundException extends Exception {

    private static final long serialVersionUID = 2749033618324612994L;

    public ComputerNotFoundException() {
        super();
    }

    public ComputerNotFoundException(String message) {
        super(message);
    }

    public ComputerNotFoundException(Throwable cause) {
        super(cause);
    }

    public ComputerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComputerNotFoundException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
