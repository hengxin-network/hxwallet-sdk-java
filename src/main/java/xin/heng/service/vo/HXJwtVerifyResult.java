package xin.heng.service.vo;

import xin.heng.service.HXConstants;

public class HXJwtVerifyResult {

    private HXJwt jwt;
    private boolean passed = false;
    private int code = 0;
    private String message;

    public HXJwtVerifyResult(HXJwt hxJwt) {
        jwt = hxJwt;
        passed = true;
        code = HXConstants.JwtVerifyCode.PASSED;
    }

    public HXJwtVerifyResult(boolean passed, int code) {
        this.passed = passed;
        this.code = code;
    }

    public HXJwtVerifyResult(HXJwt hxjwt, boolean passed, int code, String message) {
        jwt = hxjwt;
        this.passed = passed;
        this.code = code;
        this.message = message;
    }


    public HXJwt getJwt() {
        return jwt;
    }

    public void setJwt(HXJwt jwt) {
        this.jwt = jwt;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
