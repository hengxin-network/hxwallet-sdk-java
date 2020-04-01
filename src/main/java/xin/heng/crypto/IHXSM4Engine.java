package xin.heng.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;

public interface IHXSM4Engine {

    SecretKey generateKey();

    void updateCipher(byte[] key) throws InvalidKeyException;

    byte[] encrypt(byte[] rawData) throws BadPaddingException, IllegalBlockSizeException;

    byte[] decrypt(byte[] encryptData) throws BadPaddingException, IllegalBlockSizeException;
}
