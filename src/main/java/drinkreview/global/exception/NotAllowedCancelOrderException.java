package drinkreview.global.exception;

public class NotAllowedCancelOrderException extends RuntimeException {

    public NotAllowedCancelOrderException() {
        super();
    }

    public NotAllowedCancelOrderException(String message) {
        super(message);
    }

    public NotAllowedCancelOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAllowedCancelOrderException(Throwable cause) {
        super(cause);
    }

    protected NotAllowedCancelOrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
