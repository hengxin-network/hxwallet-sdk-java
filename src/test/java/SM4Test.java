import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import xin.heng.HXWallet;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Base64;

public class SM4Test {

    static String SM4TestKeyHex = "e9f552d9217722b23adb93765ae61a3d";
    static String SM4TestKeyBase64 = "6fVS2SF3IrI625N2WuYaPQ==";
    static final byte[] IV = new byte[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
    static HXWallet wallet;

    public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {

        String rawDataString = "这句话将使用SM4加密";
//        byte[] testKey =  new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

        Security.addProvider(new BouncyCastleProvider());
        wallet = HXWallet.getInstance();
        wallet.initDefaultInjects();

        SecretKey secretKey = wallet.generateSM4Key();
        secretKey.getAlgorithm();
        byte[] key = secretKey.getEncoded();
        System.out.println();
        System.out.println("Algorithm: " + secretKey.getAlgorithm());
        System.out.println("Format: " +secretKey.getFormat());
        System.out.println("base64 encoded key:" + Base64.getMimeEncoder().encodeToString(key));
        System.out.println("Hex encoded Key:" + Hex.toHexString(key));
        System.out.println("Default IV: " + Hex.toHexString(IV));
        wallet.updateSM4Cipher(key,IV);

        System.out.println();
        byte[] result = wallet.encryptBySM4(rawDataString.getBytes());
        System.out.println("Hex encrypt data: " + Hex.toHexString(result));

        byte[] decryptData = wallet.decryptBySM4(result);
        System.out.println("Hex decrypt data: " + new String(decryptData));

        String encrypt = wallet.encryptBySM4(rawDataString);
        System.out.println("base64 encoded data: " + encrypt);

        String raw = wallet.decryptBySM4(encrypt);
        System.out.println("decode base64 data: " + raw);

    }
}
