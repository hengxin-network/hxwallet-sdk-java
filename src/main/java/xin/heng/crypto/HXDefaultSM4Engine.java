package xin.heng.crypto;

import xin.heng.HXUtils;
import xin.heng.HXWallet;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class HXDefaultSM4Engine implements IHXSM4Engine {
    public static int DEFAULT_KEY_SIZE = 128;
    public static String ALGORITHM_NAME = "SM4";
    public static String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS7Padding";
    public static String ALGORITHM_NAME_CBC_PADDING = "SM4/CBC/PKCS7Padding";

    private KeyGenerator keyGenerator;

    private Cipher encryptCipher;
    private Cipher decryptCipher;


    public void injectKeyGenerator(KeyGenerator generator) {
        this.keyGenerator = generator;
    }

    public void injectDefault() {
        try {
            keyGenerator = KeyGenerator.getInstance("SM4", "BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            HXUtils.log(e);
        }
    }

    @Override
    public SecretKey generateKey() {
        keyGenerator.init(DEFAULT_KEY_SIZE, new SecureRandom());
        return keyGenerator.generateKey();
    }

    @Override
    public void updateCipher(byte[] key, byte[] iv) throws InvalidKeyException {
        try {
            encryptCipher = Cipher.getInstance(ALGORITHM_NAME_CBC_PADDING, "BC");
            decryptCipher = Cipher.getInstance(ALGORITHM_NAME_CBC_PADDING, "BC");

            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            Key encryptKeySpec = new SecretKeySpec(key, ALGORITHM_NAME);
            encryptCipher.init(Cipher.ENCRYPT_MODE, encryptKeySpec, ivParameterSpec);

            Key decryptKeySpec = new SecretKeySpec(key, ALGORITHM_NAME);
            decryptCipher.init(Cipher.DECRYPT_MODE, decryptKeySpec, ivParameterSpec);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidAlgorithmParameterException e) {
            HXUtils.log(e);
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

    @Override
    public byte[] encrypt(byte[] key, byte[] iv, byte[] rawData) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_CBC_PADDING, "BC");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Key encryptKeySpec = new SecretKeySpec(key, ALGORITHM_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, encryptKeySpec, ivParameterSpec);
            return cipher.doFinal(rawData);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            HXUtils.log(e);
        }
        return null;
    }

    @Override
    public byte[] encrypt(byte[] key, byte[] rawData) {
        return encrypt(key, HXWallet.IV, rawData);
    }

    @Override
    public byte[] decrypt(byte[] key, byte[] iv, byte[] encryptData) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_CBC_PADDING, "BC");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Key decryptKeySpec = new SecretKeySpec(key, ALGORITHM_NAME);
            cipher.init(Cipher.DECRYPT_MODE, decryptKeySpec, ivParameterSpec);
            return cipher.doFinal(encryptData);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            HXUtils.log(e);
        }
        return null;
    }

    @Override
    public byte[] decrypt(byte[] key, byte[] encryptData) {
        return decrypt(key, HXWallet.IV, encryptData);
    }
}
