package xin.heng.service;

import xin.heng.HXUtils;
import xin.heng.service.dto.HXResponse;
import xin.heng.service.vo.HXBaseUrl;
import xin.heng.service.vo.HXFileHolder;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HXDefaultHttpClient implements IHXHttpClient {

    HXBaseUrl baseUrl;

    @Override
    public void setBaseUrl(HXBaseUrl baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public HXBaseUrl getBaseUrl() {
        return baseUrl;
    }

    @Override
    public HXResponse<String> get(String path, Map<String, String> headers, Map<String, String> queries) {
        try {
            String urlPath = HXUtils.buildUrlPathWithQueries(path, queries);
            URL url = HXUtils.packageRequestUrl(this, urlPath);
            System.out.println(url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (headers != null) {
                headers.forEach((k, v) -> {
                    connection.setRequestProperty(k, v);
                });
            }
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //得到响应流
                InputStream inputStream = connection.getInputStream();
                //将响应流转换成字符串
                String result = HXUtils.is2String(inputStream);//将流转换为字符串。
                HXResponse<String> response = new HXResponse<>();
                response.httpCode = responseCode;
                response.responseBody = result;
                return response;
            } else {
                InputStream errorStream = connection.getErrorStream();
                String error = HXUtils.is2String(errorStream);
                HXResponse<String> response = new HXResponse<>();
                response.httpCode = responseCode;
                response.responseBody = error;
                return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
            HXResponse<String> response = new HXResponse<>();
            response.originError = e;
            return response;
        }
    }

    @Override
    public HXResponse<String> post(String path, Map<String, String> queries, Map<String, String> headers, byte[] body) {
        try {
            String urlPath = HXUtils.buildUrlPathWithQueries(path, queries);
            URL url = HXUtils.packageRequestUrl(this, urlPath);
            System.out.println(url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (headers != null) {
                headers.forEach((k, v) -> {
                    connection.setRequestProperty(k, v);
                });
            }

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.connect();

            if (body != null) {
                DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
                writer.write(body);
                writer.flush();
                writer.close();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //得到响应流
                InputStream inputStream = connection.getInputStream();
                //将响应流转换成字符串
                String result = HXUtils.is2String(inputStream);//将流转换为字符串。
                HXResponse<String> response = new HXResponse<>();
                response.httpCode = responseCode;
                response.responseBody = result;
                connection.disconnect();
                return response;
            } else {
                InputStream errorStream = connection.getErrorStream();
                String error = HXUtils.is2String(errorStream);
                HXResponse<String> response = new HXResponse<>();
                response.httpCode = responseCode;
                response.responseBody = error;
                connection.disconnect();
                return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
            HXResponse<String> response = new HXResponse<>();
            response.originError = e;
            return response;
        }
    }
}
