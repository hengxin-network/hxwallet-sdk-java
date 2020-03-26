package xin.heng.service.vo;

import java.net.URL;
import java.util.Map;

public class HXJwtVerifyMaterial {
    private String rawJwtString;
    private String requestMethod;
    private String url;
    private Map<String, Object> body;
    private boolean verifySig = true;

    public String getRawJwtString() {
        return rawJwtString;
    }

    public HXJwtVerifyMaterial setRawJwtString(String rawJwtString) {
        this.rawJwtString = rawJwtString;
        return this;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public HXJwtVerifyMaterial setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public HXJwtVerifyMaterial setUrl(String url) {
        this.url = url;
        return this;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public HXJwtVerifyMaterial setBody(Map<String, Object> body) {
        this.body = body;
        return this;
    }

    public boolean isVerifySig() {
        return verifySig;
    }

    public HXJwtVerifyMaterial setVerifySig(boolean verifySig) {
        this.verifySig = verifySig;
        return this;
    }
}
