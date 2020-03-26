package xin.heng.service.dto;

public class HXResponse<T> {
    public T responseBody;
    public int httpCode;
    public Exception originError = null;

    public boolean isSuccess() {
        return originError == null && httpCode >= 200 && httpCode < 300;
    }
}
