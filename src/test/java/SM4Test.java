import org.bouncycastle.jce.provider.BouncyCastleProvider;
import xin.heng.HXWallet;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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

        String rawDataString = "这句话是使用sm4算法加密的";

        Security.addProvider(new BouncyCastleProvider());
        wallet = HXWallet.getInstance();
        wallet.initDefaultInjects();

//        byte[] key = Hex.decode(SM4TestKeyHex);
        byte[] key = Base64.getMimeDecoder().decode(SM4TestKeyBase64);

        wallet.updateSM4Cipher(key);

        byte[] result = wallet.encryptBySM4(rawDataString.getBytes());
        System.out.println("encrypt data: " + Base64.getMimeEncoder().encodeToString(result));

        byte[] decryptData = wallet.decryptBySM4(result);
        System.out.println("decrypt data: " + new String(decryptData));
    }
}
