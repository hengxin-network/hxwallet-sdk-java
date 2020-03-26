package xin.heng.service.vo;

public class HXJwtHeader {
    /**
     * Algorithm
     * eg: SM2
     */
    private String alg;
    /**
     * Type
     * eg: JWT
     */
    private String typ;

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }
}
