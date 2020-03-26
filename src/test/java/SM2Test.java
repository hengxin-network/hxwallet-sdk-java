import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.util.encoders.Hex;
import xin.heng.HXWallet;
import xin.heng.crypto.HXDefaultKeyFactory;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class SM2Test {


    static {
    }

    static SM2Engine engine = new SM2Engine(SM2Engine.Mode.C1C3C2);

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

            byte[] encryptData = encryptByPublicKey(testData.getBytes(), publicKey);
            System.out.println("Encrypt data HexString: " + Hex.toHexString(encryptData));
            System.out.println("Encrypt data Base64: " + Base64.getMimeEncoder().encodeToString(encryptData));

            byte[] decryptData = decryptByPrivateKey(encryptData, privateKey);
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
            System.out.println("verify signature result: " + HXWallet.getInstance().verifyBySM2(testData.getBytes(),result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] encryptByPublicKey(byte[] data, PublicKey key) {
        ECPublicKeyParameters ecPublicKeyParameters = null;
        if (key instanceof BCECPublicKey) {
            BCECPublicKey publicKey = (BCECPublicKey) key;
            ECParameterSpec s = publicKey.getParameters();
            ECDomainParameters domainParameters = new ECDomainParameters(s.getCurve(), s.getG(), s.getN());
            ecPublicKeyParameters = new ECPublicKeyParameters(publicKey.getQ(), domainParameters);
        }
        engine.init(true, new ParametersWithRandom(ecPublicKeyParameters, new SecureRandom()));
        byte[] result;

        try {
            result = engine.processBlock(data, 0, data.length);
            return result;
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] decryptByPrivateKey(byte[] data, PrivateKey key) {
        ECPrivateKeyParameters ecPrivateKeyParameters = null;
        if (key instanceof BCECPrivateKey) {
            BCECPrivateKey privateKey = (BCECPrivateKey) key;
            ECParameterSpec s = privateKey.getParameters();
            ECDomainParameters domainParameters = new ECDomainParameters(s.getCurve(), s.getG(), s.getN());
            ecPrivateKeyParameters = new ECPrivateKeyParameters(privateKey.getD(), domainParameters);
        }
        engine.init(false, ecPrivateKeyParameters);
        byte[] result;

        try {
            result = engine.processBlock(data, 0, data.length);
            return result;
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            return null;
        }
    }

}
