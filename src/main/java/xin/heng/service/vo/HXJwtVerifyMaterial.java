package xin.heng.service.vo;

public class HXJwtVerifyMaterial {
    private String rawJwtString;
    private String requestMethod;
    private String url;
    private byte[] body;
    private boolean useIssuerToVerify;
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

    public byte[] getBody() {
        return body;
    }

    public HXJwtVerifyMaterial setBody(byte[] body) {
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

    public boolean isUseIssuerToVerify() {
        return useIssuerToVerify;
    }

    public HXJwtVerifyMaterial setUseIssuerToVerify(boolean useIssuerToVerify) {
        this.useIssuerToVerify = useIssuerToVerify;
        return this;
    }
}
