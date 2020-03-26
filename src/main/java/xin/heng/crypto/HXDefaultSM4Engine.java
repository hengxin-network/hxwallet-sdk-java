package xin.heng.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class HXDefaultSM4Engine implements IHXSM4Engine {
    public static int DEFAULT_KEY_SIZE = 128;
    public static String ALGORITHM_NAME = "SM4";
    public static String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS7Padding";

    private Cipher encryptCipher;
    private Cipher decryptCipher;

    @Override
    public void updateCipher(byte[] key) throws InvalidKeyException {
        try {
            encryptCipher = Cipher.getInstance(ALGORITHM_NAME_ECB_PADDING, "BC");
            decryptCipher = Cipher.getInstance(ALGORITHM_NAME_ECB_PADDING, "BC");

            Key encryptKeySpec = new SecretKeySpec(key, ALGORITHM_NAME);
            encryptCipher.init(Cipher.ENCRYPT_MODE, encryptKeySpec);

            Key decryptKeySpec = new SecretKeySpec(key, ALGORITHM_NAME);
            decryptCipher.init(Cipher.DECRYPT_MODE, decryptKeySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] encrypt(byte[] rawData) throws BadPaddingException, IllegalBlockSizeException {
        return encryptCipher.doFinal(rawData);
    }

    @Override
    public byte[] decrypt(byte[] encryptData) throws BadPaddingException, IllegalBlockSizeException {
        return decryptCipher.doFinal(encryptData);
    }
}
