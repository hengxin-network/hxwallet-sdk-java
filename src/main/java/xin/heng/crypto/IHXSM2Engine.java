package xin.heng.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public interface IHXSM2Engine {

    byte[] encrypt(byte[] data, PublicKey key);

    byte[] decrypt(byte[] data);

    void setPrivateKey(PrivateKey key);

    void setPrivateKey(String encodedKey) throws InvalidKeySpecException;
}
