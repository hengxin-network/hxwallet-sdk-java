package xin.heng;

import xin.heng.service.HXConstants;
import xin.heng.service.IHXHttpClient;
import xin.heng.service.error.InvalidAddressException;
import xin.heng.service.error.JwtNotValidException;
import xin.heng.service.vo.*;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
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
        if (client.getBaseUrl().getPort() == HXBaseUrl.UNSPECIFIED) {
            return new URL(client.getBaseUrl().getSchema() + "://" + client.getBaseUrl().getHost() + path);
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
            String sigBase64 = wallet.digestBySM3(new String(rawSig));
            payloadJson = payloadJson + ",\"sig\":\"" + sigBase64 + "\"}";
        } else {
            payloadJson = payloadJson + "}";
        }

        String headerJson = "{\"alg\":\"HXJWT\",\"typ\":\"JWT\"}";

        String headerStr = Base64.getUrlEncoder().encodeToString(headerJson.getBytes(UTF_8));
        String payloadStr = Base64.getUrlEncoder().encodeToString(payloadJson.getBytes(UTF_8));
        String preJwt = (headerStr + "." + payloadStr).replace("=", "");

        //sm2签名
        byte[] sign = wallet.signBySM2(preJwt.getBytes());
        if (sign.length != 64) sign = sm2RsAsn1ToPlain(sign);
        String signature = Base64.getUrlEncoder().encodeToString(sign);
        //拼接token
        String token = preJwt + "." + signature;

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
            String sigBase64 = wallet.digestBySM3(new String(rawSig));
            jwtPayload.setSig(sigBase64);
            payloadJson = payloadJson + ",\"sig\":\"" + sigBase64 + "\"}";
        } else {
            payloadJson = payloadJson + "}";
        }
        jwtHeader.setAlg("HXJWT");
        jwtHeader.setTyp("JWT");

        String headerJson = "{\"alg\":\"HXJWT\",\"typ\":\"JWT\"}";

        String headerStr = Base64.getUrlEncoder().encodeToString(headerJson.getBytes(UTF_8));
        String payloadStr = Base64.getUrlEncoder().encodeToString(payloadJson.getBytes(UTF_8));
        String preJwt = (headerStr + "." + payloadStr).replace("=", "");

        //sm2签名
        byte[] sign = wallet.signBySM2(preJwt.getBytes());
        if (sign.length != 64) sign = sm2RsAsn1ToPlain(sign);
        String signature = Base64.getUrlEncoder().encodeToString(sign);

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
            return new HXJwtVerifyResult(null, false, HXConstants.JwtVerifyCode.WRONG_FORMAT, "jwt should be a 3-segment string, connected by dot(.)");
        }
        String headerBase64 = jwts[0];
        String payloadBase64 = jwts[1];
        String signature = jwts[2];
        String headerRawString = new String(Base64.getUrlDecoder().decode(headerBase64));
        HXJwtHeader header = HXUtils.optFromJson(headerRawString, HXJwtHeader.class);
        String payloadRawString = new String(Base64.getUrlDecoder().decode(payloadBase64));
        HXJwtPayload payload = HXUtils.optFromJson(payloadRawString, HXJwtPayload.class);

        HXJwt jwt = new HXJwt();
        jwt.setRaw(material.getRawJwtString());
        jwt.setHeader(header);
        jwt.setPayload(payload);
        jwt.setSignature(signature);

        if (!"HXJWT".contentEquals(header.getAlg())) {
            return new HXJwtVerifyResult(jwt, false, HXConstants.JwtVerifyCode.WRONG_ALGORITHM, "hengxin-network jwt algorithm should use HXJWT.");
        }
        if (!"JWT".contentEquals(header.getTyp())) {
            return new HXJwtVerifyResult(jwt, false, HXConstants.JwtVerifyCode.WRONG_JWT_TYPE, "hengxin-network jwt type should use JWT.");
        }
        String preJwt = headerBase64 + "." + payloadBase64;

        boolean verifyResult = false;
        try {
            if (material.isUseIssuerToVerify()) {
                byte[] publickey = wallet.decodeAddress(payload.getIss());
                wallet.setSM2SignerPublicKey(Base64.getMimeEncoder().encodeToString(publickey));
            }
            byte[] sig = Base64.getUrlDecoder().decode(signature);
            if (sig.length == RS_LEN * 2) sig = sm2RsPlainToAsn1(sig);
            verifyResult = wallet.verifyBySM2(preJwt.getBytes(), sig);
        } catch (SignatureException e) {
            verifyResult = false;
        } catch (InvalidAddressException e) {
            e.printStackTrace();
            verifyResult = false;
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            verifyResult = false;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            verifyResult = false;
        }

        if (!verifyResult) {
            return new HXJwtVerifyResult(jwt, false, HXConstants.JwtVerifyCode.WRONG_SIGNATURE_NOT_VALID, "jwt signature not valid");
        }

        if (System.currentTimeMillis() > payload.getExp() * 1000) {
            return new HXJwtVerifyResult(jwt, false, HXConstants.JwtVerifyCode.WRONG_EXPIRED, "jwt has expired");
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
            String digest = wallet.digestBySM3(new String(rawSig));
            if (!sig.contentEquals(digest)) {
                return new HXJwtVerifyResult(jwt, false, HXConstants.JwtVerifyCode.WRONG_PAYLOAD_SIG_NOT_VALID, "jwt payload sig not valid.");
            }
        }

        return new HXJwtVerifyResult(jwt);
    }

    public static byte[] convertJsonData(Map<String, Object> body) {
        if (body == null || body.isEmpty()) return new byte[0];
        return HXUtils.optToJson(body).getBytes();
    }

    public static byte[] convertMultiPartFormData(Map<String, Object> body, HXFileHolder fileHolder) throws IOException {
        String end = "\r\n";
        String boundary;
        if (fileHolder != null && fileHolder.getBoundary() != null && fileHolder.getBoundary().length() != 0) {
            boundary = fileHolder.getBoundary();
        } else {
            boundary = UUID.randomUUID().toString();
        }

        StringBuilder prev = new StringBuilder();

        final StringBuilder formDataBuilder = new StringBuilder();
        byte[] fileBytes;
        if (fileHolder != null && fileHolder.getFile() != null) {
            if (fileHolder.getFile().length() > Integer.MAX_VALUE) {
                throw new IOException("File too big, please slice file first.");
            }
            prev.append("--");
            prev.append(boundary);
            prev.append(end);
            prev.append("Content-Disposition: form-data; name=\"file\"; filename=\"");
            prev.append(fileHolder.getUploadName());
            prev.append("\"\r\n");
            prev.append("Content-Type: application/octet-stream");
            prev.append(end + end);

            int fileLength = (int) fileHolder.getFile().length();
            fileBytes = new byte[fileLength];
            FileInputStream is = new FileInputStream(fileHolder.getFile());
            is.read(fileBytes);
            is.close();

            formDataBuilder.append(end); // 给文件末尾换行
        } else {
            fileBytes = new byte[0];
        }

        body.forEach((k, v) -> {
            formDataBuilder.append("--");
            formDataBuilder.append(boundary);
            formDataBuilder.append(end);
            formDataBuilder.append("Content-Disposition: form-data; name=\"");
            formDataBuilder.append(k);
            formDataBuilder.append("\"");
            formDataBuilder.append(end + end);
            if (v instanceof String) {
                formDataBuilder.append((String) v);
            } else {
                formDataBuilder.append(HXUtils.optToJson(v));
            }

            formDataBuilder.append(end);
        });

        StringBuilder after = new StringBuilder();
        after.append("--" + boundary + "--\r\n\r\n");

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


    private static final int RS_LEN = 32;
    private static final int ASN1_OBJECT = 48;
    private static final int ASN1_INTEGER = 2;
    private static final byte[] ASN1_START = new byte[2];
    private static final byte[] ASN1_KEY = new byte[2];

    public static byte[] sm2RsPlainToAsn1(byte[] plain) {
        boolean rZero = plain[0] < 0;
        boolean sZero = plain[32] < 0;
        int totalLength = 68;
        if (rZero) totalLength++;
        if (sZero) totalLength++;
        byte[] asn1 = new byte[totalLength + 2];
        int cursor = 4;
        asn1[0] = ASN1_OBJECT;
        asn1[1] = (byte) totalLength;
        asn1[2] = ASN1_INTEGER;
        asn1[3] = (byte) (rZero ? RS_LEN + 1 : RS_LEN);
        if (rZero) {
            asn1[cursor] = 0;
            cursor++;
        }
        System.arraycopy(plain, 0, asn1, cursor, RS_LEN);
        cursor += RS_LEN;
        asn1[cursor] = ASN1_INTEGER;
        asn1[cursor + 1] = (byte) (sZero ? RS_LEN + 1 : RS_LEN);
        cursor += 2;
        if (sZero) {
            asn1[cursor] = 0;
            cursor++;
        }
        System.arraycopy(plain, RS_LEN, asn1, cursor, RS_LEN);
        return asn1;
    }

    public static byte[] sm2RsAsn1ToPlain(byte[] asn1) {
        byte[] plain = new byte[2 * RS_LEN];
        int cursor = 4;
        int rLength = asn1[3];
        if (rLength == 33) {
            cursor++;
        }
        System.arraycopy(asn1, cursor, plain, 0, RS_LEN);
        cursor += RS_LEN;
        int sLength = asn1[cursor + 1];
        cursor += 2;
        if (sLength == 33) {
            cursor++;
        }
        System.arraycopy(asn1, cursor, plain, RS_LEN, RS_LEN);
        return plain;
    }

}