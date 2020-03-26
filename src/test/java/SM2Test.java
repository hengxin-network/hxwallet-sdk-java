import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import xin.heng.HXWallet;
import xin.heng.crypto.HXDefaultKeyFactory;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class SM2Test {

    public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException {
        String testData = "this is an another test Data string.";
        Security.addProvider(new BouncyCastleProvider());
        HXWallet.getInstance().initDefaultInjects();

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

            byte[] encryptData = HXWallet.getInstance().encryptBySM2(testData.getBytes(), publicKey);
            System.out.println("Encrypt data HexString: " + Hex.toHexString(encryptData));
            System.out.println("Encrypt data Base64: " + Base64.getMimeEncoder().encodeToString(encryptData));

            HXWallet.getInstance().setSM2EnginePrivateKey(privateKey);
            byte[] decryptData = HXWallet.getInstance().decryptBySM2(encryptData);
            System.out.println("Decrypt data: " + new String(decryptData));
            System.out.println("Decrypt data HexString: " + Hex.toHexString(decryptData));
            System.out.println("Decrypt data Base64: " + Base64.getMimeEncoder().encodeToString(decryptData));


            HXWallet.getInstance().injectSM2Engine(new HXDefaultSM2Engine());
            HXWallet.getInstance().setSM2PrivateKey(TestUtil.pemTestKey);

            byte[] result = HXWallet.getInstance().signBySM2(testData.getBytes());

            System.out.println("length: " + result.length);
            System.out.println(Arrays.toString(result));
            String stringResult = Hex.toHexString(result);
            System.out.println(stringResult);

            HXWallet.getInstance().setSM2SignerPublicKey(TestUtil.testPublicKey);
            System.out.println("verify signature result: " + HXWallet.getInstance().verifyBySM2(testData.getBytes(), result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
