import org.bouncycastle.jce.provider.BouncyCastleProvider;
import xin.heng.HXWallet;
import xin.heng.crypto.HXDefaultKeyFactory;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SM2Test {

    public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException {
        String testData = "this is an another test Data string.";
        Security.addProvider(new BouncyCastleProvider());
        HXWallet.getInstance().initDefaultInjects();
        HXWallet.getInstance().injectSM2Engine(new HXDefaultSM2Engine());
        try {
            String encoded = TestUtil.pemTestKey;
            encoded = encoded.replace("-----BEGIN PRIVATE KEY-----", "");
            encoded = encoded.replace("-----END PRIVATE KEY-----", "");
            byte[] decode = Base64.getMimeDecoder().decode(encoded);
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decode);

            String publicEncoded = TestUtil.testPublicKey;
            publicEncoded = publicEncoded.replace("-----BEGIN PUBLIC KEY-----", "");
            publicEncoded = publicEncoded.replace("-----END PUBLIC KEY-----", "");
            byte[] publicDecode = Base64.getMimeDecoder().decode(publicEncoded);
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicDecode);

            HXDefaultKeyFactory keyFactory = new HXDefaultKeyFactory();
            keyFactory.injectDefault();

            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            String encryptData = HXWallet.getInstance().encryptBySM2(testData, publicKey);
            System.out.println("Encrypt data Base64: " + encryptData);

            HXWallet.getInstance().setSM2EnginePrivateKey(privateKey);
            String decryptData = HXWallet.getInstance().decryptBySM2(encryptData);
            System.out.println("Decrypt data: " + decryptData);

            HXWallet.getInstance().injectSM2Engine(new HXDefaultSM2Engine());
            HXWallet.getInstance().setSM2PrivateKey(TestUtil.pemTestKey);

            String result = HXWallet.getInstance().signBySM2(testData);

            System.out.println("length: " + result.length());
            System.out.println(result);

            HXWallet.getInstance().setSM2SignerPublicKey(TestUtil.testPublicKey);
            System.out.println("verify signature result: " + HXWallet.getInstance().verifyBySM2(testData, result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
