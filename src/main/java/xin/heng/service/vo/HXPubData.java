package xin.heng.service.vo;

public class HXPubData<T> {
    private String t; // type
    private T d; // data
    private String h; // hash

    public String getT() {
        return t;
    }

    public HXPubData setT(String t) {
        this.t = t;
        return this;
    }

    public T getD() {
        return d;
    }

    public HXPubData setD(T d) {
        this.d = d;
        return this;
    }

    public String getH() {
        return h;
    }

    public HXPubData setH(String h) {
        this.h = h;
        return this;
    }
}
