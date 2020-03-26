package xin.heng.service.vo;

public class HXTransactionMemo {
    private String t; // type
    private String d; // data
    private String h; // hash

    public String getT() {
        return t;
    }

    public HXTransactionMemo setT(String t) {
        this.t = t;
        return this;
    }

    public String getD() {
        return d;
    }

    public HXTransactionMemo setD(String d) {
        this.d = d;
        return this;
    }

    public String getH() {
        return h;
    }

    public HXTransactionMemo setH(String h) {
        this.h = h;
        return this;
    }
}
