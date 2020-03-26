package xin.heng.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public interface IHXKeyFactory {

    PrivateKey generatePrivate(KeySpec spec) throws InvalidKeySpecException;

    PublicKey generatePublic(KeySpec spec) throws InvalidKeySpecException;
}
