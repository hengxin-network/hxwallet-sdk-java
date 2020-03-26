package xin.heng.service;

public class HXConstants {

    public static String HTTP_METHOD_GET = "GET";
    public static String HTTP_METHOD_POST = "POST";
    
    public static class ErrorCode {
        public static int INTERNAL = 1;
        public static int INVALID_PARAM = 2;
        public static int UNAUTHORIZED = 3;
        public static int PERMISSION_DENIED = 4;

        public static int INSUFFICIENT_BALANCE = 10202;
        public static int TRACE_PAID_BY_OTHERS = 10203;
    }
}
