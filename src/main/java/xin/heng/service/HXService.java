package xin.heng.service;

import xin.heng.HXUtils;
import xin.heng.HXWallet;
import xin.heng.service.dto.*;
import xin.heng.service.vo.HXBaseUrl;
import xin.heng.service.vo.HXJwtBuildMaterial;
import xin.heng.service.vo.HXTransaction;
import xin.heng.service.vo.HXTransactionMemo;

import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.HashMap;

public class HXService {

    private HXWallet wallet;
    private IHXHttpClient httpClient;

    private static long DEFAULT_EXPIRED_TIME = 7200L;
    private long expiredTime = DEFAULT_EXPIRED_TIME;

    public HXService() {
        wallet = HXWallet.getInstance();
        httpClient = new HXDefaultHttpClient();
    }

    public HXService(HXWallet injectWallet) {
        wallet = injectWallet;
        httpClient = new HXDefaultHttpClient();
    }

    public HXService(IHXHttpClient client) {
        wallet = HXWallet.getInstance();
        httpClient = client;
    }

    public HXService(HXWallet injectWallet, IHXHttpClient client) {
        wallet = injectWallet;
        httpClient = client;
    }

    public void setBaseUrl(HXBaseUrl baseUrl) {
        if (httpClient != null) {
            httpClient.setBaseUrl(baseUrl);
        }
    }

    public void injectWallet(HXWallet hxWallet) {
        wallet = hxWallet;
    }

    public void injectHttpClient(IHXHttpClient client) {
        httpClient = client;
    }

    public void setJwtExpiredTime(Long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public HXResponse<HXUserInfoBody> getInfo(String address) throws SignatureException, MalformedURLException {
        HXJwtBuildMaterial jwtMaterial = new HXJwtBuildMaterial();
        jwtMaterial.setAddress(address)
                .setExpiredTime(expiredTime)
                .setBody(null)
                .setRequestMethod(HXConstants.HTTP_METHOD_GET)
                .setUrl(HXUtils.buildUrlPathWithQueries("/info", null));

        String jwtToken = HXUtils.buildJwtString(wallet, jwtMaterial);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwtToken);
        headers.put("Content-Type", "application/json;charset=utf-8");

        HXResponse<String> stringHXResponse = httpClient.get("/info", headers, null);
        HXResponse<HXUserInfoBody> response = new HXResponse<>();
        response.httpCode = stringHXResponse.httpCode;
        response.originError = stringHXResponse.originError;
        if (stringHXResponse.responseBody != null && stringHXResponse.responseBody.length() != 0) {
            response.responseBody = HXUtils.optFromJson(stringHXResponse.responseBody, HXUserInfoBody.class);
        }
        return response;
    }

    public HXResponse<HXResponseBody<HXTransaction>> postTransactions(String address, HXTransactionRequest requestMap) throws SignatureException, MalformedURLException {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("asset", requestMap.getAsset());
        bodyMap.put("opponent_addresses", requestMap.getOpponent_addresses());
        bodyMap.put("trace_id", requestMap.getTrace_id());
        String memoString = "{\"t\":\"" + requestMap.getMemo().getT() + "\",\"h\":\"" + requestMap.getMemo().getH() + "\",\"d\":\"" + requestMap.getMemo().getD() + "\"}";
        bodyMap.put("memo", memoString);

        HXJwtBuildMaterial jwtMaterial = new HXJwtBuildMaterial();
        jwtMaterial.setAddress(address)
                .setExpiredTime(expiredTime)
                .setBody(bodyMap)
                .setRequestMethod(HXConstants.HTTP_METHOD_POST)
                .setUrl(HXUtils.buildUrlPathWithQueries("/transactions", null));

        String jwtToken = HXUtils.buildJwtString(wallet, jwtMaterial);
        HashMap<String, String> headers = new HashMap<>();
        System.out.println("jwt: " + jwtToken);
        headers.put("Authorization", "Bearer " + jwtToken);
        headers.put("Content-Type", "application/json;charset=utf-8");

        HXResponse<String> stringHXResponse = httpClient.post("/transactions", null, headers, bodyMap);
        HXResponse<HXResponseBody<HXTransaction>> response = new HXResponse<>();
        response.httpCode = stringHXResponse.httpCode;
        response.originError = stringHXResponse.originError;
        if (stringHXResponse.responseBody != null && stringHXResponse.responseBody.length() != 0) {
            response.responseBody = HXUtils.optFromJson(stringHXResponse.responseBody, HXTransactionBody.class);
        }
        return response;
    }

    public HXResponse<HXSnapshotsBody> getSnapshots(String address, HXSnapshotRequest requestMap) throws SignatureException, MalformedURLException {
        HashMap<String, String> queries = new HashMap<>();
        queries.put("from", String.valueOf(requestMap.getFrom()));
        queries.put("limit", String.valueOf(requestMap.getLimit()));
        queries.put("order", requestMap.getOrder());
        if (requestMap.getAsset() != null && requestMap.getAsset().length() != 0) {
            queries.put("asset", requestMap.getAsset());
        }

        HXJwtBuildMaterial jwtMaterial = new HXJwtBuildMaterial();
        jwtMaterial.setAddress(address)
                .setExpiredTime(expiredTime)
                .setBody(null)
                .setRequestMethod(HXConstants.HTTP_METHOD_GET)
                .setUrl(HXUtils.buildUrlPathWithQueries("/snapshots", queries));

        String jwtToken = HXUtils.buildJwtString(wallet, jwtMaterial);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwtToken);
        headers.put("Content-Type", "application/json;charset=utf-8");

        HXResponse<String> stringResponse = httpClient.get("/snapshots", headers, queries);
        HXResponse<HXSnapshotsBody> snapshotsResponse = new HXResponse<>();
        snapshotsResponse.httpCode = stringResponse.httpCode;
        snapshotsResponse.originError = stringResponse.originError;
        if (stringResponse.responseBody != null && stringResponse.responseBody.length() != 0) {
            snapshotsResponse.responseBody = HXUtils.optFromJson(stringResponse.responseBody, HXSnapshotsBody.class);
            try {
                snapshotsResponse.responseBody.data.forEach(snapshot -> {
                    snapshot.setParsed_memo(HXUtils.optFromJson(snapshot.getMemo(), HXTransactionMemo.class));
                });
            } catch (Exception e) {
//                     do nothing;
            }

        }
        return snapshotsResponse;
    }
}
