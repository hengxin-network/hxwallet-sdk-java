package xin.heng.sample.crypto;

import xin.heng.HXWallet;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.util.Base64;

public class SM4Sample {


    public static byte[] encrypt(byte[] rawString) {
        try {
            System.out.println("=======SM4 encrypt Sample=======");
            System.out.println("raw String: ");
            byte[] encrypt = HXWallet.getInstance().encryptBySM4(rawString);
            System.out.println("encrypt base64: " + Base64.getMimeEncoder().encodeToString(encrypt));
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

    public static byte[] decrypt(byte[] encryptData) {
        try {
            System.out.println("=======SM4 decrypt Sample=======");
            byte[] decrypt = HXWallet.getInstance().decryptBySM4(encryptData);
            System.out.println("decrypt String: " + new String(decrypt));
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
