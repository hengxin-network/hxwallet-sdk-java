import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import xin.heng.HXUtils;
import xin.heng.HXWallet;
import xin.heng.service.HXDefaultHttpClient;
import xin.heng.service.IHXHttpClient;
import xin.heng.service.vo.*;

import java.net.URL;
import java.security.Security;

public class JWTTokenTest {

    static HXWallet wallet;

    public static void main(String[] args) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            HXUtils.injectJsonParser(new JsonParser());
            wallet = HXWallet.getInstance();
            wallet.initDefaultInjects();
            wallet.injectSM2Engine(new HXDefaultSM2Engine());

            IHXHttpClient iclient = new HXDefaultHttpClient();
            iclient.setBaseUrl(new HXBaseUrl("http", "106.14.59.4", 8930));

            wallet.setSM2SignerPrivateKey(TestUtil.pemTestKey);
            wallet.setSM2SignerPublicKey(TestUtil.testPublicKey2);

            HXJwtBuildMaterial buildMaterial = new HXJwtBuildMaterial()
                    .setRequestMethod("GET")
                    .setBody(null)
                    .setAddress(TestUtil.userAddress)
                    .setExpiredTime(7200L)
                    .setUrl(HXUtils.buildUrlPathWithQueries("/info", null));
            System.out.println("=======Build========");
            HXJwt jwtToken = HXUtils.buildJwt(wallet, buildMaterial);
            System.out.println("build result: " + HXUtils.optToJson(jwtToken));
            System.out.println("=======Build========");
            System.out.println();

//            String jwtRawStr = "eyJhbGciOiJTTTIiLCJ0eXAiOiJKV1QifQ.eyJleHAiOjE1ODY1MjU1NjgsImlhdCI6MTU4NjQzOTE2OCwiaXNzIjoiSFgyREtZQ0FwMmIzNlRIWU1LZFZzTThLVUVGN205MmFGNU1rVE1XRk43ZXg3eHR0WXVzSFJ3eTE5a1lHalpDUHhZRGV6Z1pxUGdwS0xTRk1NRnRiQUhGRDQxbkVLWlc5RVBVUUxaUWUxdWhwN2JVNFdDOFBONGp2S2UyakdkeGQ0S3RkcHhubUc4VEplbUNYcWJnalBhblF4TDZCRUpBQkZQQkRFUmdQaDZXdnN3cE5kaVpDRE5NYlRZVlZBTXYzaXpnSHVtU3I2MjJHdno4eG44MnJSa1lRYUZGbiIsImp0aSI6IjIwNTc2NjExLWVlNTMtNDk5Ni05YTI0LWYwZjA5YmU5YTMzYiIsInNpZyI6Ii9PeXBhRURmYThrY1BmeEwvekovVi9uWEltZlhucTJ3eHpZc0laRXJoOTQ9In0.MEUCIQD5TK2eY7zKMgW6fYnO4DIbf61M99guUlH6kzVy9RdDwwIgNLQq20C5lUdkY2Cpak2x7vqlXlD8LPcVXHSRAn9rHa0";
//            String jwtTestBody = "{\"cipher_encrypted\":\"BKPB1bTypkxjHVev09t5rB4I7W9/HKyn5YKMTW8vumzjxTwsUj0EByD0QLTmVFE2uQFbNEKOSttU/eODBSXA/ZgSbp721xcC7avEGAvlCtTNnKtW61X51pRH7kSJJPQv6yIh8c06XRdZzdxCbeZRkm4=\",\"data_encrypted\":\"i4yLriLxdsNI6hfZoCyS6vGZCxw1J91+eRaFXAJHYCRNb24IVi8TurFdtWNWayFuQ4k2UTmH6w3CnoO0MBaEKO7WUHj4uwZm7mJhb7sxeDjqdQstgSpDOt+SbW0EadNb\",\"data_encrypted_method\":\"sm4\"}";
//
//            HXJwtVerifyMaterial verifyMaterial = new HXJwtVerifyMaterial()
//                    .setRequestMethod("GET")
//                    .setBody(jwtTestBody.getBytes())
//                    .setRawJwtString(jwtRawStr)
//                    .setUrl(HXUtils.buildUrlPathWithQueries("/info", null));
//
//            System.out.println("=======Verify=======");
//            HXJwtVerifyResult jwtResult = HXUtils.verifyJwt(wallet, verifyMaterial);
//            System.out.println("verify passed: " + jwtResult.isPassed());
//            System.out.println("verify jwt result: " + HXUtils.optToJson(jwtResult));
//            System.out.println("=======Verify=======");
//            System.out.println();

            URL url = new URL("http://106.14.59.4:8930/info");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().get().url(url)
                    .addHeader("Authorization", "Bearer " + jwtToken.getRaw())
                    .build();
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
