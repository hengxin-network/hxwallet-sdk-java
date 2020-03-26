package xin.heng.sample.crypto;

import org.bouncycastle.util.encoders.Hex;
import xin.heng.HXWallet;

import java.security.SignatureException;
import java.util.Arrays;

public class SM2Sample {

    public static byte[] getSm2Sign(String signData) {
        try {
            System.out.println("==========SM2 sign Sample==========");
            System.out.println("sm2 sign sign_data: " + signData);
            byte[] result = HXWallet.getInstance().signBySM2(signData.getBytes());
            System.out.println("sm2 sign length: " + result.length);
            System.out.println("sm2 sign byte array: " + Arrays.toString(result));
            String stringResult = Hex.toHexString(result);
            System.out.println("sm2 sign hex string: " + stringResult);
            System.out.println("==========SM2 sign Sample==========");
            System.out.println();


            return result;
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }
}
