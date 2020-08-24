import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.util.encoders.Hex;
import xin.heng.HXUtils;
import xin.heng.HXWallet;
import xin.heng.service.HXDefaultHttpClient;
import xin.heng.service.IHXHttpClient;
import xin.heng.service.vo.*;

import java.math.BigInteger;
import java.net.URL;
import java.security.KeyFactory;
import java.security.Security;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;

public class JWTTokenTest {

    static HXWallet wallet;

    public static void main(String[] args) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            HXUtils.injectJsonParser(new JsonParser());
            wallet = HXWallet.getInstance();
            wallet.initDefaultInjects();
            wallet.injectSM2Engine(new HXDefaultSM2Engine());

            X9ECParameters sm2 = GMNamedCurves.getByName("sm2p256v1");
            ECParameterSpec sm2Spec = new ECParameterSpec(sm2.getCurve(), sm2.getG(), sm2.getN());

            byte[] pub = Hex.decode("02b966d6418f74bc9eedac72531add0e4417f3accf38f49479a3ac76f5ceb72896");
            final X9ECParameters x9ECParameters = ECUtil.getNamedCurveByName("sm2p256v1");
            final ECCurve curve = x9ECParameters.getCurve();
            final ECPoint point = EC5Util.convertPoint(curve.decodePoint(pub));

            // 根据曲线恢复公钥格式
            final ECNamedCurveSpec ecSpec = new ECNamedCurveSpec("sm2p256v1", curve, x9ECParameters.getG(), x9ECParameters.getN());

            byte[] decode = Hex.decode("00aa0fd187cbdc8517e060949f9881ff56fd8001454d7abc8fd1775e3a2f7f49e5");
            ECPrivateKeySpec keySpec = new ECPrivateKeySpec(new BigInteger(1, decode), sm2Spec);
            KeyFactory kf = KeyFactory.getInstance("EC", "BC");
            BCECPrivateKey privateKey = (BCECPrivateKey) kf.generatePrivate(keySpec);
            BCECPublicKey publicKey = (BCECPublicKey) kf.generatePublic(new ECPublicKeySpec(point, ecSpec));
            wallet.setSM2PrivateKey(privateKey);
            wallet.setSM2SignerPublicKey(publicKey);

            IHXHttpClient iclient = new HXDefaultHttpClient();
            iclient.setBaseUrl(new HXBaseUrl("https", "hxwallet.testnet.heng.xin"));

            HXJwtBuildMaterial buildMaterial = new HXJwtBuildMaterial()
                    .setRequestMethod("GET")
                    .setBody(null)
                    .setAddress(TestUtil.userAddress)
                    .setExpiredTime(7200L)
                    .setUrl(HXUtils.buildUrlPathWithQueries("/info", null));
            System.out.println("=======Build========");
            HXJwt jwtToken = HXUtils.buildJwt(wallet, buildMaterial);
            System.out.println("build result: " + HXUtils.optToJson(jwtToken));
            System.out.println("sig length: " + jwtToken.getPayload().getSig());
            System.out.println("signature length: " + jwtToken.getSignature().getBytes().length);
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
            System.out.println("verify jwt result: " + jwtResult.getJwt().getRaw());
            System.out.println("=======Verify=======");
            System.out.println();

            URL url = new URL("https://hxwallet.testnet.heng.xin/info");
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
