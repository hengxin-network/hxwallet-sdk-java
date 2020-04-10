package xin.heng.service.vo;

public class HXPubData<T> {
    private String t; // type
    private T d; // data

    public String getT() {
        return t;
    }

    public HXPubData<T> setT(String t) {
        this.t = t;
        return this;
    }

    public T getD() {
        return d;
    }

    public HXPubData<T> setD(T d) {
        this.d = d;
        return this;
    }

}
