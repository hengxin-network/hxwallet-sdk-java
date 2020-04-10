import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
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
            String encoded = TestUtil.testPrivateKey;
            encoded = encoded.replace("-----BEGIN PRIVATE KEY-----", "");
            encoded = encoded.replace("-----END PRIVATE KEY-----", "");
            byte[] decode = Base64.getMimeDecoder().decode(encoded);
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decode);

//            String publicEncoded = TestUtil.testPublicKey;
//            publicEncoded = publicEncoded.replace("-----BEGIN PUBLIC KEY-----", "");
//            publicEncoded = publicEncoded.replace("-----END PUBLIC KEY-----", "");
//            byte[] publicDecode = Base64.getMimeDecoder().decode(publicEncoded);
//            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicDecode);
//
//            byte[] decodePublicKey = HXWallet.getInstance().decodeAddress(TestUtil.testAddress);
//            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decodePublicKey);
//
//            HXDefaultKeyFactory keyFactory = new HXDefaultKeyFactory();
//            keyFactory.injectDefault();
//
//            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
//            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
//
//            String encryptData = HXWallet.getInstance().encryptBySM2(testData, publicKey);
//            System.out.println("Encrypt data Base64: " + encryptData);
//
//            HXWallet.getInstance().setSM2EnginePrivateKey(privateKey);
//            String decryptData = HXWallet.getInstance().decryptBySM2(TestUtil.testData);
//            byte[] dataBytes = decryptData.getBytes();
//            System.out.println("Decrypt data: " + Base64.getMimeEncoder().encodeToString(dataBytes));
//
//            HXWallet.getInstance().injectSM2Engine(new HXDefaultSM2Engine());
//            HXWallet.getInstance().setSM2PrivateKey(TestUtil.pemTestKey);
//
//            byte[] result = HXWallet.getInstance().signBySM2(testData.getBytes());
//
//            System.out.println("length: " + result.length);
//            System.out.println(Base64.getMimeEncoder().encodeToString(result));
//            System.out.println();


            String testPublicKey = "-----BEGIN PUBLIC KEY-----\n" +
                    "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAESVltMs5lwWWngmIDAUvKrtyNseXC\n" +
                    "3vZnq8KW8CU5e5I+dlvQLSoelABXIN2TBnjKvR76kKWwc5j5BtYYOkF9hA==\n" +
                    "-----END PUBLIC KEY-----";
            HXWallet.getInstance().setSM2SignerPublicKey(testPublicKey);
            String testHexData = "4c90f4364636bfe2a00a4e08b51cab1bceabc715a8e19d1a63fddcde5a284fdc";
            String testHexSig = "3045022100f94cad9e63bcca3205ba7d89cee0321b7fad4cf7d82e5251fa933572f51743c3022034b42adb40b99547646360a96a4db1eefaa55e50fc2cf7155c7491027f6b1dad";
            System.out.println("verify signature result: " + HXWallet.getInstance().verifyBySM2(Hex.decode(testHexData), Hex.decode(testHexSig)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
