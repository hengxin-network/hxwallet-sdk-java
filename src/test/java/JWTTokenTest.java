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
//            System.out.println("sig length: " + jwtToken.getPayload().getSig().length());
//            System.out.println("signature length: " + jwtToken.getSignature().getBytes());
            System.out.println("=======Build========");
            System.out.println();

            HXJwtVerifyMaterial verifyMaterial = new HXJwtVerifyMaterial()
                    .setRequestMethod("GET")
                    .setBody(null)
                    .setRawJwtString(jwtToken.getRaw())
                    .setUrl(HXUtils.buildUrlPathWithQueries("/info", null));

            System.out.println("=======Verify=======");
            HXJwtVerifyResult jwtResult = HXUtils.verifyJwt(wallet, verifyMaterial);
            System.out.println("verify passed: " + jwtResult.isPassed());
            System.out.println("verify jwt result: " + HXUtils.optToJson(jwtResult));
            System.out.println("=======Verify=======");
            System.out.println();

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
