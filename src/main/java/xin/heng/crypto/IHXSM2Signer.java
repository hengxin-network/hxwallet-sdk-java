package xin.heng.crypto;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public interface IHXSM2Signer {
    byte[] sign(byte[] rawData) throws SignatureException;

    boolean verify(byte[]rawData,byte[] signature) throws SignatureException;

    void injectKeyFactory(IHXKeyFactory keyFactory);

    void setPrivateKey(PrivateKey key) throws InvalidKeyException;

    void setPrivateKey(String encodedKey) throws InvalidKeySpecException, InvalidKeyException;

    void setPublicKey(PublicKey key) throws InvalidKeyException;

    void setPublicKey(String encodedKey) throws InvalidKeySpecException, InvalidKeyException;
}
