import org.bouncycastle.jce.provider.BouncyCastleProvider;
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

    static HXWallet wallet;

    public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {

        String rawDataString = "这句话是使用SM4算法加密的";

        Security.addProvider(new BouncyCastleProvider());
        wallet = HXWallet.getInstance();
        wallet.initDefaultInjects();

        SecretKey secretKey = wallet.generateSM4Key();
        byte[] key = secretKey.getEncoded();

        assert "SM4".contentEquals(secretKey.getAlgorithm());
        System.out.println(secretKey.getAlgorithm());
        System.out.println(Base64.getMimeEncoder().encodeToString(key));
        System.out.println(secretKey.getFormat());

        wallet.updateSM4Cipher(key);

        String result = wallet.encryptBySM4(rawDataString);
        System.out.println("encrypt data: " + result);

        String decryptData = wallet.decryptBySM4(result);
        System.out.println("decrypt data: " + decryptData);
    }
}
