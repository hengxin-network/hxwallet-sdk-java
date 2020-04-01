package xin.heng.service;

public final class HXConstants {
    private HXConstants() {
    }

    public static String HTTP_METHOD_GET = "GET";
    public static String HTTP_METHOD_POST = "POST";

    public static final class ErrorCode {
        private ErrorCode() {
        }

        public static int INTERNAL = 1;
        public static int INVALID_PARAM = 2;
        public static int UNAUTHORIZED = 3;
        public static int PERMISSION_DENIED = 4;

        public static int INSUFFICIENT_BALANCE = 10202;
        public static int TRACE_PAID_BY_OTHERS = 10203;
    }

    public static final class JwtVerifyCode {
        private JwtVerifyCode() {
        }

        public static int PASSED = 0;
        public static int WRONG_FORMAT = 1;
        public static int WRONG_ALGORITHM = 2;
        public static int WRONG_JWT_TYPE = 3;
        public static int WRONG_SIGNATURE_NOT_VALID = 4;
        public static int WRONG_EXPIRED = 5;
        public static int WRONG_PAYLOAD_SIG_NOT_VALID = 6;
    }


}
