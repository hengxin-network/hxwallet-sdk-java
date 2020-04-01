package xin.heng.sample.crypto;

import org.bouncycastle.util.encoders.Hex;
import xin.heng.HXWallet;

import java.util.Arrays;

public class SM3Sample {
    public static String digest(String message) {
        System.out.println("==========SM3 digest Sample==========");
        System.out.println("sm3 digest message data: " + message);
        String result = HXWallet.getInstance().digestBySM3(message);
        System.out.println("sm3 digest length: " + result.length());
        System.out.println("sm3 digest result: " + result);
        System.out.println("==========SM3 digest Sample==========");
        System.out.println();
        return result;
    }
}
