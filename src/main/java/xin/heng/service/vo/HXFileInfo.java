package xin.heng.service.vo;

public class HXFileInfo {

    private int dp;
    private String rh;
    private String h;
    private long s;

    public String getH() {
        return h;
    }

    public HXFileInfo setH(String h) {
        this.h = h;
        return this;
    }

    public long getS() {
        return s;
    }

    public HXFileInfo setS(long s) {
        this.s = s;
        return this;
    }

    public String getRh() {
        return rh;
    }

    public HXFileInfo setRh(String rh) {
        this.rh = rh;
        return this;
    }

    public int getDp() {
        return dp;
    }

    public HXFileInfo setDp(int dp) {
        this.dp = dp;
        return this;
    }
}
