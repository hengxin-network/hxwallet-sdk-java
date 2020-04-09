package xin.heng.service.error;

public class InvalidAddressException extends Exception {
    public InvalidAddressException() {
    }

    public InvalidAddressException(String message) {
        super(message);
    }

    public InvalidAddressException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAddressException(Throwable cause) {
        super(cause);
    }

    public InvalidAddressException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
