package xin.heng.crypto;

import xin.heng.HXUtils;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class HXDefaultSM2Signer implements IHXSM2Signer {

    private Signature sm2PrivateSignature;
    private Signature sm2PublicSignature;
    private IHXKeyFactory keyFactory;

    public void injectDefault() throws NoSuchProviderException, NoSuchAlgorithmException {
        injectSignSignature(Signature.getInstance("SM3withSM2", "BC"));
        injectVerifySignature(Signature.getInstance("SM3withSM2", "BC"));
        HXDefaultKeyFactory kf = new HXDefaultKeyFactory();
        kf.injectDefault();
        keyFactory = kf;
    }

    public void injectSignSignature(Signature privateKeySignature) {
        sm2PrivateSignature = privateKeySignature;
    }

    public void injectVerifySignature(Signature publicKeySignature) {
        sm2PublicSignature = publicKeySignature;
    }

    @Override
    public byte[] sign(byte[] rawData) throws SignatureException {
        sm2PrivateSignature.update(rawData);
        return sm2PrivateSignature.sign();
    }

    @Override
    public boolean verify(byte[] rawData, byte[] signature) throws SignatureException {
        sm2PublicSignature.update(rawData);
        return sm2PublicSignature.verify(signature);
    }

    @Override
    public void injectKeyFactory(IHXKeyFactory keyFactory) {
        this.keyFactory = keyFactory;
    }

    @Override
    public void setPublicKey(PublicKey key) throws InvalidKeyException {
        sm2PublicSignature.initVerify(key);
    }

    @Override
    public void setPublicKey(String encodedKey) throws InvalidKeySpecException, InvalidKeyException {
        encodedKey = encodedKey.replace("-----BEGIN PUBLIC KEY-----", "");
        encodedKey = encodedKey.replace("-----END PUBLIC KEY-----", "");
        byte[] publicDecode = Base64.getMimeDecoder().decode(encodedKey);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicDecode);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        setPublicKey(publicKey);
    }

    @Override
    public void setPrivateKey(PrivateKey key) throws InvalidKeyException {
        if (sm2PrivateSignature != null) {
            sm2PrivateSignature.initSign(key);
        }
    }

    @Override
    public void setPrivateKey(String encodedKey) throws InvalidKeySpecException, InvalidKeyException {
        encodedKey = encodedKey.replace("-----BEGIN PRIVATE KEY-----", "");
        encodedKey = encodedKey.replace("-----END PRIVATE KEY-----", "");

        byte[] decode = Base64.getMimeDecoder().decode(encodedKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decode);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        setPrivateKey(privateKey);
    }

    @Override
    public byte[] sign(PrivateKey key, byte[] rawData) {
        try {
            Signature sig = Signature.getInstance("SM3withSM2", "BC");
            sig.initSign(key);
            sig.update(rawData);
            return sig.sign();
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] sign(String encodedKey, byte[] rawData) {
        try {
            encodedKey = encodedKey.replace("-----BEGIN PRIVATE KEY-----", "");
            encodedKey = encodedKey.replace("-----END PRIVATE KEY-----", "");
            byte[] decode = Base64.getMimeDecoder().decode(encodedKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decode);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return sign(privateKey, rawData);
        } catch (InvalidKeySpecException e) {
            HXUtils.log(e);
        }
        return null;
    }

    @Override
    public boolean verify(PublicKey key, byte[] rawData, byte[] signature) {
        try {
            Signature sig = Signature.getInstance("SM3withSM2", "BC");
            sig.initVerify(key);
            sig.update(rawData);
            return sig.verify(signature);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | SignatureException e) {
            HXUtils.log(e);
        }
        return false;
    }

    @Override
    public boolean verify(String encodedKey, byte[] rawData, byte[] signature) {
        try {
            encodedKey = encodedKey.replace("-----BEGIN PUBLIC KEY-----", "");
            encodedKey = encodedKey.replace("-----END PUBLIC KEY-----", "");
            byte[] publicDecode = Base64.getMimeDecoder().decode(encodedKey);
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicDecode);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            return verify(publicKey, rawData, signature);
        } catch (InvalidKeySpecException e) {
            HXUtils.log(e);
        }
        return false;
    }

}
