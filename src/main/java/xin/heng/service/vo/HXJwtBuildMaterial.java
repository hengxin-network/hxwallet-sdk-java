package xin.heng.service.vo;

import java.net.URL;
import java.util.Map;
import java.util.UUID;

public class HXJwtBuildMaterial {
    private String address;
    private long expiredTime;
    private String jti = UUID.randomUUID().toString();
    private String requestMethod;
    private String url;
    private Map<String, Object> body;
    private boolean requestSignature = true;

    public String getAddress() {
        return address;
    }

    public HXJwtBuildMaterial setAddress(String address) {
        this.address = address;
        return this;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public HXJwtBuildMaterial setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
        return this;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public HXJwtBuildMaterial setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
        return this;
    }

    public String getJti() {
        return jti;
    }

    public HXJwtBuildMaterial setJti(String jti) {
        this.jti = jti;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public HXJwtBuildMaterial setUrl(String url) {
        this.url = url;
        return this;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public HXJwtBuildMaterial setBody(Map<String, Object> body) {
        this.body = body;
        return this;
    }

    public boolean isRequestSignature() {
        return requestSignature;
    }

    public HXJwtBuildMaterial setRequestSignature(boolean requestSignature) {
        this.requestSignature = requestSignature;
        return this;
    }
}
