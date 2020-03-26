package xin.heng.sample.business;

import xin.heng.HXUtils;
import xin.heng.sample.FastJsonParser;
import xin.heng.service.HXService;
import xin.heng.service.dto.HXResponse;
import xin.heng.service.dto.HXUserInfoBody;

import java.net.MalformedURLException;
import java.security.SignatureException;

public class GetInfoSample {

    public static HXResponse<HXUserInfoBody> getInfo(HXService service, String userAddress) {
        try {
            HXResponse<HXUserInfoBody> response = service.getInfo(userAddress);
            System.out.println("Http Status Code: " + response.httpCode);
            if (response.isSuccess()) {
                System.out.println("getInfo result: " + HXUtils.optToJson(response.responseBody));
            } else {
                if (response.originError != null) { // 如IO错误等Exception会记录在这里
                    response.originError.printStackTrace(System.err);
                } else {
                    // 如果是业务异常则依然存放在responseBody里，
                    // responseBody中的 code即为业务错误码
                    System.err.println(FastJsonParser.getInstance().optToJson(response.responseBody));
                }
            }
            System.out.println();
            return response;
        } catch (SignatureException e) {
            e.printStackTrace();
            // JWT Token签名失败，需要检查sm2 sm3 privateKey factory是否注入以及是否正常运作
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
