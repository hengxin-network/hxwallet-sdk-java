package xin.heng.sample.crypto;

import xin.heng.HXWallet;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class SM4Sample {


    public static String encrypt(String rawString) {
        try {
            System.out.println("=======SM4 encrypt Sample=======");
            System.out.println("raw String: ");
            String encrypt = HXWallet.getInstance().encryptBySM4(rawString);
            System.out.println("encrypt result : " + encrypt);
            System.out.println("=======SM4 encrypt Sample=======");
            System.out.println();
            return encrypt;
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String encryptData) {
        try {
            System.out.println("=======SM4 decrypt Sample=======");
            String decrypt = HXWallet.getInstance().decryptBySM4(encryptData);
            System.out.println("decrypt String: " + decrypt);
            System.out.println("=======SM4 decrypt Sample=======");
            System.out.println();
            return decrypt;
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
