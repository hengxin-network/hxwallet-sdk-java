package xin.heng.sample.crypto;

import xin.heng.HXWallet;

import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;

public class PrivateKeySample {

    public static void update(String encoded) {
        try {
            HXWallet.getInstance().setSM2PrivateKey(encoded);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

}
