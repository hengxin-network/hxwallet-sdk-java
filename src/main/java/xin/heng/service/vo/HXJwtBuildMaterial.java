package xin.heng.service.vo;

import java.security.PrivateKey;
import java.util.UUID;

public class HXJwtBuildMaterial {
    private String address;
    private long expiredTime;
    private String jti = UUID.randomUUID().toString();
    private String requestMethod;
    private String url;
    private byte[] body;
    private boolean requestSignature = true;

    // 这两个Key传一个
    private String encodedKey;
    private PrivateKey privateKey;

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

    public byte[] getBody() {
        return body;
    }

    public HXJwtBuildMaterial setBody(byte[] body) {
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

    public String getEncodedKey() {
        return encodedKey;
    }

    public HXJwtBuildMaterial setEncodedKey(String encodedKey) {
        this.encodedKey = encodedKey;
        return this;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public HXJwtBuildMaterial setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
        return this;
    }
}
