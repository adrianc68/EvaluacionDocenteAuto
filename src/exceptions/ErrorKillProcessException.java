package exceptions;

public class ErrorKillProcessException extends Exception {

    public ErrorKillProcessException() {
    }

    public ErrorKillProcessException(String message) {
        super(message);
    }

    public ErrorKillProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorKillProcessException(Throwable cause) {
        super(cause);
    }

    public ErrorKillProcessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
