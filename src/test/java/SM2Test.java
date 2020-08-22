import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;
import org.bouncycastle.util.encoders.Hex;
import xin.heng.HXWallet;
import xin.heng.crypto.HXDefaultKeyFactory;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SM2Test {

    public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException, IOException {
        Security.addProvider(new BouncyCastlePQCProvider());
        Security.addProvider(new BouncyCastleProvider());
        HXWallet.getInstance().initDefaultInjects();
        HXWallet.getInstance().injectSM2Engine(new HXDefaultSM2Engine());
        HXDefaultKeyFactory keyFactory = new HXDefaultKeyFactory();
        keyFactory.injectDefault();
        String testData = "haha.c1c2c3";

        String encryptDataHex = "042f854ec1513910f9a63d81dacc7d4d90877e1b165f8b14e8f6c5a843d1b9598bab915dfba5e7c633aeea1a6eb65aba5d0d2ea6a8d4f189213765da23da5163c2e5bee851fec6fc42af2359c16110da759ddb21e217d43897f4cdd7fd70667f066f4bff09";
//        String encryptDataHex = "04f28370a13ea81ff3bc0e79c60957dc9e25d2b61d7815661ccba9ea4660469ca9f5f902b1750e4277caf009df4f66a61cb9879ffa656c50ed6fc8c73914e9a306664332de7cfb8cea1eb3b4bcbbaf2cd2c7e0c83376609ce9331483534385abc2fdff1143";

        HXWallet.getInstance().setSM2PrivateKey(TestUtil.pemTestKey);
        byte[] decrypt = HXWallet.getInstance().decryptBySM2(Hex.decode(encryptDataHex));
        System.out.println(new String(decrypt));
//        byte[] bytes = HXWallet.getInstance().signBySM2(testData.getBytes(StandardCharsets.UTF_8));

//        System.out.println("hex sign:" + Hex.toHexString(bytes));

//        String pk = TestUtil.pemTestKey;
//        pk = pk.replace("-----BEGIN PRIVATE KEY-----", "")
//                .replace("-----END PRIVATE KEY-----","");
//        byte[] decode = Base64.getMimeDecoder().decode(pk);
//        System.out.println("private key: " + Hex.toHexString(decode));

        String encodedKey = TestUtil.testPublicKey;
        encodedKey = encodedKey.replace("-----BEGIN PUBLIC KEY-----", "");
        encodedKey = encodedKey.replace("-----END PUBLIC KEY-----", "");
        byte[] publicDecode = Base64.getMimeDecoder().decode(encodedKey);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicDecode);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        byte[] bytes = HXWallet.getInstance().encryptBySM2(testData.getBytes(), publicKey);
        System.out.println(Hex.toHexString(bytes));

    }
}
