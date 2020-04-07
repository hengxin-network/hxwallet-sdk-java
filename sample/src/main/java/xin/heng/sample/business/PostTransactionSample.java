package xin.heng.sample.business;

import xin.heng.HXUtils;
import xin.heng.HXWallet;
import xin.heng.sample.FastJsonParser;
import xin.heng.service.HXService;
import xin.heng.service.dto.HXResponse;
import xin.heng.service.dto.HXSnapshotBody;
import xin.heng.service.dto.HXTransactionRequest;
import xin.heng.service.vo.HXFileHolder;
import xin.heng.service.vo.HXPubData;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.UUID;

public class PostTransactionSample {

    public static HXResponse<HXSnapshotBody> postTransaction(HXService service, String userAddress, String opponentAddress, String asset) {
        HXPubData memo = new HXPubData()
                .setT("the type") // t -> type
                .setD("the data") // d -> data
                .setH(HXWallet.getInstance().digestBySM3("the data")); // h -> hash

        HXTransactionRequest request = new HXTransactionRequest()
                .setAsset(asset) // asset
                .setPub_data(memo) // memo为HXTransactionMemo 根据不同业务要传入不同的参数
                .setOpponent_addresses(Arrays.asList(opponentAddress)) // opponent_addresses为相关address的列表
                .setTrace_id(UUID.randomUUID().toString()); // trace_id
        try {
            HXResponse<HXSnapshotBody> response = service.postTransactions(userAddress, request, null);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static HXResponseBody<>


    public static HXResponse<HXSnapshotBody> postTransactionWithFile(HXService service, String userAddress, String opponentAddress, String asset) {
        HXPubData memo = new HXPubData()
                .setT("test-fileupload-type") // t -> type
                .setD("test-fileData-hxwalletJar") // d -> data
                .setH(HXWallet.getInstance().digestBySM3("test-fileData-hxwalletJar")); // h -> hash

        HXTransactionRequest request = new HXTransactionRequest()
                .setAsset(asset) // asset
                .setPub_data(memo) // memo为HXTransactionMemo 根据不同业务要传入不同的参数
                .setOpponent_addresses(Arrays.asList(opponentAddress)) // opponent_addresses为相关address的列表
                .setTrace_id(UUID.randomUUID().toString()); // trace_id

        File file = new File("./src/main/lib/hxwallet.jar");
        HXFileHolder fileHolder = new HXFileHolder()
                .setFile(file)
                .setUploadName("hxwallet.jar");

        try {
            HXResponse<HXSnapshotBody> response = service.postTransactions(userAddress, request, fileHolder);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
