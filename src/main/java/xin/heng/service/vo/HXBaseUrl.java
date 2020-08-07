package xin.heng.service.vo;

public class HXBaseUrl {
    public static final int UNSPECIFIED = -1;
    private String schema = "http";
    private String host;
    private int port;

    public HXBaseUrl(String schema, String host, int port) {
        setSchema(schema);
        setHost(host);
        setPort(port);
    }

    public HXBaseUrl(String schema,String host){
        setSchema(schema);
        setHost(host);
        setPort(UNSPECIFIED);
    }

    public HXBaseUrl setSchema(String schema) {
        this.schema = schema;
        return this;
    }

    public HXBaseUrl setHost(String host) {
        this.host = host;
        return this;
    }

    public HXBaseUrl setPort(int port) {
        this.port = port;
        return this;
    }

    public String getSchema() {
        return schema;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
