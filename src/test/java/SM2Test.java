import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import xin.heng.HXWallet;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class SM2Test {

    public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        String testData = "haha:0";
        Security.addProvider(new BouncyCastlePQCProvider());
        Security.addProvider(new BouncyCastleProvider());
        HXWallet.getInstance().initDefaultInjects();
        HXWallet.getInstance().injectSM2Engine(new HXDefaultSM2Engine());

        HXWallet.getInstance().setSM2SignerPrivateKey(TestUtil.pemTestKey);
        byte[] bytes = HXWallet.getInstance().signBySM2(testData.getBytes(StandardCharsets.UTF_8));
        System.out.println("hex sign:" + Hex.toHexString(bytes));

        String pk = TestUtil.pemTestKey;
        pk = pk.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----","");
        byte[] decode = Base64.getMimeDecoder().decode(pk);
        System.out.println("private key: " + Hex.toHexString(decode));

//        String encodedKey = TestUtil.testPublicKey2;
//        encodedKey = encodedKey.replace("-----BEGIN PUBLIC KEY-----", "");
//        encodedKey = encodedKey.replace("-----END PUBLIC KEY-----", "");
//        byte[] publicDecode = Base64.getMimeDecoder().decode(encodedKey);
//        System.out.println(Hex.toHexString(publicDecode));
//        String testHexSig = "3046022100a4a57861bb764e219b7e2dbeaa0183d951266258a633332aa0f25d8591923dff022100d4c99f3c87ad80bd197bf43fefa7686409b39c3598c614fa1ada157ce90329f5";

        HXWallet.getInstance().setSM2SignerPublicKey(TestUtil.testPublicKey2);
        System.out.println("verify signature result: " + HXWallet.getInstance().verifyBySM2(testData.getBytes(), bytes));
//        System.out.println(bytes.length);
//        System.out.println("verify signature result: " + HXWallet.getInstance().verifyBySM2(testData.getBytes(), Hex.decode(testHexSig)));
//        System.out.println(Hex.decode(testHexSig).length);

    }
}
