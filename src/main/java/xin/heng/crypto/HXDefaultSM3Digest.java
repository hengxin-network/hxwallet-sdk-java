package xin.heng.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class HXDefaultSM3Digest implements IHXSM3Digest {

    private MessageDigest sm3;

    public void injectDefault() throws NoSuchProviderException, NoSuchAlgorithmException {
        inject(MessageDigest.getInstance("SM3", "BC"));
    }

    public void inject(MessageDigest digest) {
        this.sm3 = digest;
    }

    @Override
    public byte[] digest(byte[] message) {
        return sm3.digest(message);
    }
}
