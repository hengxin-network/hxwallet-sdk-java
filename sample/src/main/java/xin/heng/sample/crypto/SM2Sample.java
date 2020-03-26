package xin.heng.sample.crypto;

import org.bouncycastle.util.encoders.Hex;
import xin.heng.HXWallet;
import xin.heng.crypto.HXDefaultKeyFactory;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SM2Sample {

    public static byte[] getSm2Sign(String signData) {
        try {
            System.out.println("==========SM2 sign Sample==========");
            System.out.println("sm2 sign sign_data: " + signData);
            byte[] result = HXWallet.getInstance().signBySM2(signData.getBytes());
            System.out.println("sm2 sign length: " + result.length);
            System.out.println("sm2 sign base64: " + Base64.getMimeEncoder().encodeToString(result));
            String stringResult = Hex.toHexString(result);
            System.out.println("sm2 sign hex string: " + stringResult);
            System.out.println("==========SM2 sign Sample==========");
            System.out.println();
            return result;
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean verifySm2Sign(String signData, byte[] signature) {
        try {
            System.out.println("=========SM2 verify Sample=========");
            boolean result = HXWallet.getInstance().verifyBySM2(signData.getBytes(), signature);
            System.out.println("sm2 sign verify result: " + result);
            System.out.println("=========SM2 verify Sample=========");
            System.out.println();
            return result;
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static byte[] encrypt(byte[] rawData, String key) {
        try {
            String publicEncoded = key;
            publicEncoded = publicEncoded.replace("-----BEGIN PUBLIC KEY-----", "");
            publicEncoded = publicEncoded.replace("-----END PUBLIC KEY-----", "");
            byte[] publicDecode = Base64.getMimeDecoder().decode(publicEncoded);
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicDecode);

            HXDefaultKeyFactory keyFactory = new HXDefaultKeyFactory();
            keyFactory.injectDefault();

            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            System.out.println("=======SM2 encrypt Sample=======");
            byte[] result = HXWallet.getInstance().encryptBySM2(rawData, publicKey);
            System.out.println("encrypt result base64: " + Base64.getMimeEncoder().encodeToString(result));
            System.out.println("=======SM2 encrypt Sample=======");
            System.out.println();
            return result;
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(byte[] encryptData) {
        System.out.println("=======SM2 decrypt Sample=======");
        byte[] result = HXWallet.getInstance().decryptBySM2(encryptData);
        System.out.println("decrypt string: " + new String(result));
        System.out.println("=======SM2 decrypt Sample=======");
        System.out.println();
        return result;
    }
}
