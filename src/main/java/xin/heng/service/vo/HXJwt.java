package xin.heng.service.vo;

public class HXJwt {
    private String raw;
    private HXJwtHeader header;
    private HXJwtPayload payload;
    private String signature;

    public HXJwtHeader getHeader() {
        return header;
    }

    public HXJwt setHeader(HXJwtHeader header) {
        this.header = header;
        return this;
    }

    public HXJwtPayload getPayload() {
        return payload;
    }

    public HXJwt setPayload(HXJwtPayload payload) {
        this.payload = payload;
        return this;
    }

    public String getSignature() {
        return signature;
    }

    public HXJwt setSignature(String signature) {
        this.signature = signature;
        return this;
    }

    public String getRaw() {
        return raw;
    }

    public HXJwt setRaw(String raw) {
        this.raw = raw;
        return this;
    }
}
