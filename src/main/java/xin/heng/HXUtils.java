package xin.heng;

import xin.heng.service.HXConstants;
import xin.heng.service.IHXHttpClient;
import xin.heng.service.error.JwtNotValidException;
import xin.heng.service.vo.*;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HXUtils {
    private static IHXJsonParser jsonParser;

    public static void injectJsonParser(IHXJsonParser parser) {
        jsonParser = parser;
    }

    public static <T> T optFromJson(String jsonString, Class<T> clz) {
        if (jsonParser == null) return null;
        return jsonParser.optFromJson(jsonString, clz);
    }

    public static <T> String optToJson(T obj) {
        if (jsonParser == null) return null;
        return jsonParser.optToJson(obj);
    }


    public static String is2String(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }

    public static String buildUrlPathWithQueries(String rawPath, Map<String, String> queries) {
        String path = rawPath;
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        StringBuilder pathWithQueries = new StringBuilder(path);
        if (queries != null && !queries.isEmpty()) {
            pathWithQueries.append("?");
            for (Map.Entry<String, String> entry : queries.entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                pathWithQueries.append(k);
                pathWithQueries.append("=");
                pathWithQueries.append(v);
                pathWithQueries.append("&");
            }
            pathWithQueries.deleteCharAt(pathWithQueries.length() - 1);
        }
        return pathWithQueries.toString();
    }

    public static URL packageRequestUrl(IHXHttpClient client, String path) throws MalformedURLException {
        if (client.getBaseUrl() == null) return null;
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return new URL(client.getBaseUrl().getSchema() + "://" + client.getBaseUrl().getHost() + ":" + client.getBaseUrl().getPort() + path);
    }

    public static String formatSignURL(URL url) {
        if (url.getPort() != url.getDefaultPort()) {
            return url.toString().replace(url.getProtocol() + "://" + url.getHost() + ":" + url.getPort(), "");
        } else {
            return url.toString().replace(url.getProtocol() + "://" + url.getHost(), "");
        }
    }

    public static String buildJwtString(HXWallet wallet, HXJwtBuildMaterial jwtMaterial) throws SignatureException {
        String payloadJson = "{\"exp\":" + (new Date().getTime() / 1000L + jwtMaterial.getExpiredTime()) +
                ",\"iat\":" + new Date().getTime() / 1000 +
                ",\"iss\":\"" + jwtMaterial.getAddress() + "\"" +
                ",\"jti\":\"" + UUID.randomUUID() + "\"";
        if (jwtMaterial.isRequestSignature()) {
            byte[] rawSig = (jwtMaterial.getRequestMethod().toUpperCase() + jwtMaterial.getUrl()).getBytes();
            if (jwtMaterial.getRequestMethod().toUpperCase().contentEquals("POST") && jwtMaterial.getBody() != null) {
                byte[] temp = new byte[rawSig.length + jwtMaterial.getBody().length];
                System.arraycopy(rawSig, 0, temp, 0, rawSig.length);
                System.arraycopy(jwtMaterial.getBody(), 0, temp, rawSig.length, jwtMaterial.getBody().length);
                rawSig = temp;
            }
            System.out.println(new String(rawSig));
            byte[] sig = wallet.digestBySM3(rawSig);
            payloadJson = payloadJson + ",\"sig\":\"" + Base64.getMimeEncoder().encodeToString(sig) + "\"}";
        } else {
            payloadJson = payloadJson + "}";
        }

        System.out.println("payload: " + payloadJson);

        String headerJson = "{\"alg\":\"SM2\",\"typ\":\"JWT\"}";

        String headerStr = Base64.getUrlEncoder().encodeToString(headerJson.getBytes(UTF_8));
        String payloadStr = Base64.getUrlEncoder().encodeToString(payloadJson.getBytes(UTF_8));
        String preJwt = (headerStr + "." + payloadStr).replace("=", "");
        //sm3取hash
        byte[] jwtSM3Hash = wallet.digestBySM3(preJwt.getBytes());

        //sm2签名
        byte[] signByte = wallet.signBySM2(jwtSM3Hash);
        //拼接token
        String token = preJwt + "." + Base64.getUrlEncoder().encodeToString(signByte);

        return token.replace("=", "");
    }

    public static HXJwt buildJwt(HXWallet wallet, HXJwtBuildMaterial jwtMaterial) throws SignatureException {
        HXJwt jwt = new HXJwt();
        HXJwtHeader jwtHeader = new HXJwtHeader();
        HXJwtPayload jwtPayload = new HXJwtPayload();

        long exp = new Date().getTime() / 1000L + jwtMaterial.getExpiredTime();
        jwtPayload.setExp(exp);
        long iat = new Date().getTime() / 1000L;
        jwtPayload.setIat(iat);
        jwtPayload.setIss(jwtMaterial.getAddress());
        String uuid = UUID.randomUUID().toString();
        jwtPayload.setJti(uuid);
        String payloadJson = "{\"exp\":" + exp +
                ",\"iat\":" + iat +
                ",\"iss\":\"" + jwtMaterial.getAddress() + "\"" +
                ",\"jti\":\"" + uuid + "\"";
        if (jwtMaterial.isRequestSignature()) {
            byte[] rawSig = (jwtMaterial.getRequestMethod().toUpperCase() + jwtMaterial.getUrl()).getBytes();
            if (jwtMaterial.getRequestMethod().toUpperCase().contentEquals("POST") && jwtMaterial.getBody() != null) {
                byte[] temp = new byte[rawSig.length + jwtMaterial.getBody().length];
                System.arraycopy(rawSig, 0, temp, 0, rawSig.length);
                System.arraycopy(jwtMaterial.getBody(), 0, temp, rawSig.length, jwtMaterial.getBody().length);
                rawSig = temp;
            }
            System.out.println(new String(rawSig));
            byte[] sig = wallet.digestBySM3(rawSig);
            String sigBase64 = Base64.getMimeEncoder().encodeToString(sig);
            jwtPayload.setSig(sigBase64);
            payloadJson = payloadJson + ",\"sig\":\"" + sigBase64 + "\"}";
        } else {
            payloadJson = payloadJson + "}";
        }
        jwtHeader.setAlg("SM2");
        jwtHeader.setTyp("JWT");

        String headerJson = "{\"alg\":\"SM2\",\"typ\":\"JWT\"}";

        String headerStr = Base64.getUrlEncoder().encodeToString(headerJson.getBytes(UTF_8));
        String payloadStr = Base64.getUrlEncoder().encodeToString(payloadJson.getBytes(UTF_8));
        String preJwt = (headerStr + "." + payloadStr).replace("=", "");
        //sm3取hash
        byte[] jwtSM3Hash = wallet.digestBySM3(preJwt.getBytes());

        //sm2签名
        byte[] signByte = wallet.signBySM2(jwtSM3Hash);
        String signature = Base64.getUrlEncoder().encodeToString(signByte);
        //拼接token
        String token = preJwt + "." + signature;

        token.replace("=", "");
        jwt.setRaw(token);
        jwt.setHeader(jwtHeader);
        jwt.setPayload(jwtPayload);
        jwt.setSignature(signature);
        return jwt;
    }

    public static HXJwtVerifyResult verifyJwt(HXWallet wallet, HXJwtVerifyMaterial material) throws JwtNotValidException {
        String[] jwts = material.getRawJwtString().split("\\.");
        if (jwts.length != 3) {
            return new HXJwtVerifyResult(false, HXConstants.JwtVerifyCode.WRONG_FORMAT, "jwt should be a 3-segment string, connected by dot(.)");
        }
        String headerBase64 = jwts[0];
        String payloadBase64 = jwts[1];
        String signature = jwts[2];
        String headerRawString = new String(Base64.getUrlDecoder().decode(headerBase64));
        HXJwtHeader header = HXUtils.optFromJson(headerRawString, HXJwtHeader.class);
        if (!"SM2".contentEquals(header.getAlg())) {
            return new HXJwtVerifyResult(false, HXConstants.JwtVerifyCode.WRONG_ALGORITHM, "hengxin-network jwt algorithm should use SM2.");
        }
        if (!"JWT".contentEquals(header.getTyp())) {
            return new HXJwtVerifyResult(false, HXConstants.JwtVerifyCode.WRONG_JWT_TYPE, "hengxin-network jwt type should use JWT.");
        }

        String preJwt = headerBase64 + "." + payloadBase64;
        byte[] jwtSM3Hash = wallet.digestBySM3(preJwt.getBytes());
        try {
            wallet.verifyBySM2(jwtSM3Hash, Base64.getUrlDecoder().decode(signature));
        } catch (SignatureException e) {
            return new HXJwtVerifyResult(false, HXConstants.JwtVerifyCode.WRONG_SIGNATURE_NOT_VALID, "jwt signature not valid");
        }

        String payloadRawString = new String(Base64.getUrlDecoder().decode(payloadBase64));
        HXJwtPayload payload = HXUtils.optFromJson(payloadRawString, HXJwtPayload.class);
        if (System.currentTimeMillis() > payload.getExp() * 1000) {
            return new HXJwtVerifyResult(false, HXConstants.JwtVerifyCode.WRONG_EXPIRED, "jwt has expired");
        }

        if (material.isVerifySig()) {
            String sig = payload.getSig();
            byte[] rawSig = (material.getRequestMethod().toUpperCase() + material.getUrl()).getBytes();
            if (material.getBody() != null && material.getBody() != null) {
                byte[] temp = new byte[rawSig.length + material.getBody().length];
                System.arraycopy(rawSig, 0, temp, 0, rawSig.length);
                System.arraycopy(material.getBody(), 0, temp, rawSig.length, material.getBody().length);
                rawSig = temp;
            }
            byte[] digest = wallet.digestBySM3(rawSig);
            String digestBase64 = Base64.getMimeEncoder().encodeToString(digest);
            if (!sig.contentEquals(digestBase64)) {
                return new HXJwtVerifyResult(false, HXConstants.JwtVerifyCode.WRONG_PAYLOAD_SIG_NOT_VALID, "jwt payload sig not valid.");
            }
        }

        HXJwt jwt = new HXJwt();
        jwt.setRaw(material.getRawJwtString());
        jwt.setHeader(header);
        jwt.setPayload(payload);
        jwt.setSignature(signature);
        return new HXJwtVerifyResult(jwt);
    }

    public static byte[] packageFormData(HXFileHolder fileHolder) throws IOException {
        String end = "\r\n";
        StringBuilder prev = new StringBuilder();
        prev.append("--");
        prev.append(fileHolder.getBoundary());
        prev.append(end);
        prev.append("Content-Disposition: form-data; name=\"file\"; filename=\"");
        prev.append(fileHolder.getUploadName());
        prev.append("\"\r\n");
        prev.append("Content-Type: application/octet-stream");
        prev.append(end + end);

        if (fileHolder.getFile().length() > Integer.MAX_VALUE) {
            throw new IOException("File too big, please slice file first.");
        }
        int fileLength = (int) fileHolder.getFile().length();
        byte[] fileBytes = new byte[fileLength];
        FileInputStream is = new FileInputStream(fileHolder.getFile());
        is.read(fileBytes);
        is.close();

        StringBuilder after = new StringBuilder();
        after.append("\r\n--" + fileHolder.getBoundary() + "--\r\n\r\n");

        byte[] prevStr = prev.toString().getBytes();
        byte[] afterStr = after.toString().getBytes();
        byte[] out = new byte[prevStr.length + fileBytes.length + afterStr.length];
        System.arraycopy(prevStr, 0, out, 0, prevStr.length);
        System.arraycopy(fileBytes, 0, out, prevStr.length, fileBytes.length);
        System.arraycopy(afterStr, 0, out, prevStr.length + fileBytes.length, afterStr.length);
        return out;
    }

    public static byte[] packageFormData(Map<String, String> body, HXFileHolder fileHolder) throws IOException {
        String end = "\r\n";
        StringBuilder prev = new StringBuilder();
        prev.append("--");
        prev.append(fileHolder.getBoundary());
        prev.append(end);
        prev.append("Content-Disposition: form-data; name=\"file\"; filename=\"");
        prev.append(fileHolder.getUploadName());
        prev.append("\"\r\n");
        prev.append("Content-Type: application/octet-stream");
        prev.append(end + end);

        if (fileHolder.getFile().length() > Integer.MAX_VALUE) {
            throw new IOException("File too big, please slice file first.");
        }
        int fileLength = (int) fileHolder.getFile().length();
        byte[] fileBytes = new byte[fileLength];
        FileInputStream is = new FileInputStream(fileHolder.getFile());
        is.read(fileBytes);
        is.close();

        final StringBuilder formDataBuilder = new StringBuilder();
        formDataBuilder.append(end); // 给文件末尾换行
        body.forEach((k, v) -> {
            formDataBuilder.append("--");
            formDataBuilder.append(fileHolder.getBoundary());
            formDataBuilder.append(end);
            formDataBuilder.append("Content-Disposition: form-data; name=\"");
            formDataBuilder.append(k);
            formDataBuilder.append("\"");
            formDataBuilder.append(end + end);
            formDataBuilder.append(v);
            formDataBuilder.append(end);
        });

        StringBuilder after = new StringBuilder();
        after.append("--" + fileHolder.getBoundary() + "--\r\n\r\n");

        byte[] prevBytes = prev.toString().getBytes();
        byte[] formBytes = formDataBuilder.toString().getBytes();
        byte[] afterBytes = after.toString().getBytes();
        byte[] out = new byte[prevBytes.length + fileBytes.length + formBytes.length + afterBytes.length];
        System.arraycopy(prevBytes, 0, out, 0, prevBytes.length);
        System.arraycopy(fileBytes, 0, out, prevBytes.length, fileBytes.length);
        System.arraycopy(formBytes, 0, out, prevBytes.length + fileBytes.length, formBytes.length);
        System.arraycopy(afterBytes, 0, out, prevBytes.length + fileBytes.length + formBytes.length, afterBytes.length);

        return out;
    }

}
