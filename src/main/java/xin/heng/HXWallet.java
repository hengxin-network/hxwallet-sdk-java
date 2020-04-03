package xin.heng;

import xin.heng.crypto.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class HXWallet {

    private static class INSTANCE {
        private static HXWallet hxWallet = new HXWallet();
    }

    public static HXWallet getInstance() {
        return INSTANCE.hxWallet;
    }

    private IHXSM2Signer sm2Signer;
    private IHXSM2Engine sm2Engine;
    private IHXSM3Digest sm3;
    private IHXSM4Engine sm4;

    public void initDefaultInjects() throws NoSuchProviderException, NoSuchAlgorithmException {
        HXDefaultSM2Signer signer = new HXDefaultSM2Signer();
        signer.injectDefault();
        sm2Signer = signer;

        // sm2 默认注入需要依赖bc库，为了确保sdk的纯粹，sdk不提供默认实现，实现请参考sample项目
//        sm2Engine = new HXDefaultSM2Engine();

        HXDefaultSM3Digest digest = new HXDefaultSM3Digest();
        digest.injectDefault();
        sm3 = digest;

        HXDefaultSM4Engine sm4Engine = new HXDefaultSM4Engine();
        sm4Engine.injectDefault();
        sm4 = sm4Engine;
    }

    public void injectSM2Engine(IHXSM2Engine engine) {
        sm2Engine = engine;
    }

    public void injectSM2Signer(IHXSM2Signer signer) {
        sm2Signer = signer;
    }

    public void injectSM3(IHXSM3Digest digest) {
        sm3 = digest;
    }

    public void injectSM4(IHXSM4Engine engine) {
        sm4 = engine;
    }

    public void setSM2PrivateKey(PrivateKey privateKey) throws InvalidKeyException {
        if (sm2Signer != null && sm2Engine != null) {
            sm2Signer.setPrivateKey(privateKey);
            sm2Engine.setPrivateKey(privateKey);
        } else {
            throw new NullPointerException("you should inject sm2 signer and sm2 engine at first.");
        }
    }

    public void setSM2PrivateKey(String encodedKey) throws InvalidKeySpecException, InvalidKeyException {
        if (sm2Signer != null && sm2Engine != null) {
            sm2Signer.setPrivateKey(encodedKey);
            sm2Engine.setPrivateKey(encodedKey);
        } else {
            throw new NullPointerException("you should inject sm2 signer and sm2 engine at first.");
        }
    }

    public void setSM2SignerPrivateKey(PrivateKey privateKey) throws InvalidKeyException {
        if (sm2Signer != null) {
            sm2Signer.setPrivateKey(privateKey);
        } else {
            throw new NullPointerException("you should inject sm2 signer inject at first.");
        }
    }

    public void setSM2SignerPrivateKey(String encodedKey) throws InvalidKeySpecException, InvalidKeyException {
        if (sm2Signer != null) {
            sm2Signer.setPrivateKey(encodedKey);
        } else {
            throw new NullPointerException("you should inject sm2 signer at first.");
        }
    }

    public void setSM2SignerPublicKey(PublicKey publicKey) throws InvalidKeyException {
        if (sm2Signer != null) {
            sm2Signer.setPublicKey(publicKey);
        } else {
            throw new NullPointerException("you should inject sm2 signer at first.");
        }
    }

    public void setSM2SignerPublicKey(String encodedKey) throws InvalidKeySpecException, InvalidKeyException {
        if (sm2Signer != null) {
            sm2Signer.setPublicKey(encodedKey);
        } else {
            throw new NullPointerException("you should inject sm2 signer at first.");
        }
    }

    public void setSM2EnginePrivateKey(PrivateKey privateKey) {
        if (sm2Engine != null) {
            sm2Engine.setPrivateKey(privateKey);
        } else {
            throw new NullPointerException("you should inject sm2 engine at first.");
        }
    }

    public void setSM2EnginePrivateKey(String encodedKey) throws InvalidKeySpecException {
        if (sm2Engine != null) {
            sm2Engine.setPrivateKey(encodedKey);
        } else {
            throw new NullPointerException("you should inject sm2 engine at first.");
        }
    }

    public void updateSM4Cipher(byte[] key) throws InvalidKeyException {
        if (sm4 != null) {
            sm4.updateCipher(key);
        } else {
            throw new NullPointerException("you should inject sm4 cipher at first.");
        }
    }

    public String encryptBySM2(String rawData, PublicKey publicKey) {
        byte[] encrypt = sm2Engine.encrypt(rawData.getBytes(), publicKey);
        return Base64.getMimeEncoder().encodeToString(encrypt);
    }

    public String decryptBySM2(String encryptData) {
        byte[] decode = Base64.getMimeDecoder().decode(encryptData);
        return new String(sm2Engine.decrypt(decode));
    }

    public String encryptBySM4(String rawData) throws BadPaddingException, IllegalBlockSizeException {
        byte[] encrypt = sm4.encrypt(rawData.getBytes());
        return Base64.getMimeEncoder().encodeToString(encrypt);
    }

    public String decryptBySM4(String encryptData) throws BadPaddingException, IllegalBlockSizeException {
        byte[] decode = Base64.getMimeDecoder().decode(encryptData);
        return new String(sm4.decrypt(decode));
    }

    public String signBySM2(String rawData) throws SignatureException {
        byte[] sign = sm2Signer.sign(rawData.getBytes());
        return Base64.getMimeEncoder().encodeToString(sign);
    }

    public boolean verifyBySM2(String rawData, String signature) throws SignatureException {
        byte[] decode = Base64.getMimeDecoder().decode(signature);
        return sm2Signer.verify(rawData.getBytes(), decode);
    }

    public String digestBySM3(String message) {
        byte[] digest = sm3.digest(message.getBytes());
        return Base64.getMimeEncoder().encodeToString(digest);
    }

    public SecretKey generateSM4Key() {
        return sm4.generateKey();
    }

    byte[] signBySM2(byte[] rawData) throws SignatureException {
        return sm2Signer.sign(rawData);
    }

    public byte[] digestBySM3(byte[] message) {
        return sm3.digest(message);
    }
}
