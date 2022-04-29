package drinkreview.global.exception;

public class NotAllowedUpdateOrderException extends RuntimeException {

    public NotAllowedUpdateOrderException() {
        super();
    }

    public NotAllowedUpdateOrderException(String message) {
        super(message);
    }

    public NotAllowedUpdateOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAllowedUpdateOrderException(Throwable cause) {
        super(cause);
    }

    protected NotAllowedUpdateOrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
