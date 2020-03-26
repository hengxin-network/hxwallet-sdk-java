package xin.heng.sample.crypto;

import org.bouncycastle.util.encoders.Hex;
import xin.heng.HXWallet;

import java.util.Arrays;

public class SM3Sample {
    public static byte[] digest(String message) {
        System.out.println("==========SM3 digest Sample==========");
        System.out.println("sm3 digest message data: " + message);
        byte[] result = HXWallet.getInstance().digestBySM3(message.getBytes());
        System.out.println("sm3 digest length: " + result.length);
        System.out.println("sm3 digest array: " + Arrays.toString(result));
        String stringResult = Hex.toHexString(result);
        System.out.println("sm3 digest hexString: " + stringResult);
        System.out.println("==========SM3 digest Sample==========");
        System.out.println();
        return result;
    }
}
