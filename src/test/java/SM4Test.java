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

        String rawDataString = "这句话是使用SM4算法加密的";
//        byte[] testKey =  new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

        Security.addProvider(new BouncyCastleProvider());
        wallet = HXWallet.getInstance();
        wallet.initDefaultInjects();

        SecretKey secretKey = wallet.generateSM4Key();
        byte[] key = secretKey.getEncoded();

        System.out.println(Base64.getMimeEncoder().encodeToString(key));

        wallet.updateSM4Cipher(key,IV);

        byte[] result = wallet.encryptBySM4(rawDataString.getBytes());
        System.out.println("encrypt data: " + Hex.toHexString(result));

        byte[] decryptData = wallet.decryptBySM4(result);
        System.out.println("decrypt data: " + new String(decryptData));
    }
}
