package service;

public class PageNumberException extends Exception {

	private static final long serialVersionUID = 1L;

	public PageNumberException() {
	}

	public PageNumberException(String message) {
		super(message);
	}

	public PageNumberException(Throwable cause) {
		super(cause);
	}

	public PageNumberException(String message, Throwable cause) {
		super(message, cause);
	}

	public PageNumberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
