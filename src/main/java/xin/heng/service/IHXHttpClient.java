package xin.heng.service;

import xin.heng.service.dto.HXResponse;
import xin.heng.service.vo.HXBaseUrl;

import java.util.Map;

public interface IHXHttpClient {

    void setBaseUrl(HXBaseUrl baseUrl);

    HXBaseUrl getBaseUrl();

    HXResponse<String> get(String path, Map<String, String> headers, Map<String, String> queries);

    HXResponse<String> post(String path, Map<String, String> queries, Map<String, String> headers, byte[] body);

}
