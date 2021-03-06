package xin.heng.service.dto;


public class HXSnapshotRequest {

    public static final String ORDER_ASC = "ASC";
    public static final String ORDER_DESC = "DESC";

    private String asset;
    private int from;
    private int limit = 20;
    private boolean sig_required;
    private String order = ORDER_ASC;

    public HXSnapshotRequest setAsset(String asset) {
        this.asset = asset;
        return this;
    }

    public HXSnapshotRequest setFrom(int from) {
        this.from = from;
        return this;
    }

    public HXSnapshotRequest setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public HXSnapshotRequest setOrder(String order) {
        this.order = order;
        return this;
    }

    public String getAsset() {
        return asset;
    }

    public int getFrom() {
        return from;
    }

    public int getLimit() {
        return limit;
    }

    public String getOrder() {
        return order;
    }

    public boolean isSig_required() {
        return sig_required;
    }

    public HXSnapshotRequest setSig_required(boolean sig_required) {
        this.sig_required = sig_required;
        return this;
    }
}
