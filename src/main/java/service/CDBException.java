package service;

public class CDBException extends Exception {

	private static final long serialVersionUID = 1L;

	public CDBException() {
	}

	public CDBException(String message) {
		super(message);
	}

	public CDBException(Throwable cause) {
		super(cause);
	}

	public CDBException(String message, Throwable cause) {
		super(message, cause);
	}

	public CDBException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
