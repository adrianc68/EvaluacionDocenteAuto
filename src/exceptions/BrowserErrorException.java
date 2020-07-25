package exceptions;

public class BrowserErrorException extends Exception {

    public BrowserErrorException() {
    }

    public BrowserErrorException(String message) {
        super(message);
    }

    public BrowserErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public BrowserErrorException(Throwable cause) {
        super(cause);
    }

    public BrowserErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
