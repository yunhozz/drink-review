package drinkreview.global.exception;

public class NotShippingException extends RuntimeException {

    public NotShippingException() {
        super();
    }

    public NotShippingException(String message) {
        super(message);
    }

    public NotShippingException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotShippingException(Throwable cause) {
        super(cause);
    }

    protected NotShippingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
