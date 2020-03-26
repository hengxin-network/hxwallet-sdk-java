package xin.heng.crypto;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class HXDefaultKeyFactory implements IHXKeyFactory {


    private KeyFactory keyFactory;

    public void injectDefault() throws NoSuchProviderException, NoSuchAlgorithmException {
        keyFactory = KeyFactory.getInstance("EC", "BC");
    }

    public void inject(KeyFactory keyFactory) {
        this.keyFactory = keyFactory;
    }

    @Override
    public PrivateKey generatePrivate(KeySpec spec) throws InvalidKeySpecException {
        return keyFactory.generatePrivate(spec);
    }

    @Override
    public PublicKey generatePublic(KeySpec spec) throws InvalidKeySpecException {
        return keyFactory.generatePublic(spec);
    }

}
