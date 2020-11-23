import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import xin.heng.HXWallet;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Base64;

public class SM4Test {

    static String SM4TestKeyHex = "e9f552d9217722b23adb93765ae61a3d";
    static String SM4TestKeyBase64 = "6fVS2SF3IrI625N2WuYaPQ==";
    static final byte[] IV = new byte[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
    static HXWallet wallet;

    static String logKey = "/XFAvPBdg4Q7vTiLu2UWiw==";
    static String logEncrpt = "anhGWmlMM2dNZzl2dDlsbVdPdE9qLytoWTdMYUhhTVFWazR5ZnRlMURZeGRkVWhjMDlYY25qaG1G\n" +
            "NlFMVVZ2UlRCU05DZ1NRVEc4MGRHV0xQS2cyeVJtSHlFeklFc2NVdGZuSmFZM01oSms2VXJveVlZ\n" +
            "TG9tRDhTMnlVWU5XSDRkSUFuWHI3dU90ZmovUlRGV25CTzFXTlN6bTJOSjgram9XdEQ0YkU3NlhQ\n" +
            "L3VhK1FYTDVBamhqbFgzUFAzZnQzUW44Z1BvcWcxSlVrSy93M20zd0FIbEdrYXBGTm4xdDJXMHhs\n" +
            "eDhQVUYyMGVMS3dtVTNtWkhJUGoxcU5kdzFxZkkxMURTS08vcjB6TDU3Zmo3bXN4UFBIM3lqSU1k\n" +
            "OHRSTWZBUVhtVjI3M0FEUTk2UWFhQ09sNFZYc3N1R3g1M2JHendoMUttbUd0N1l1Wng3NllKeTdX\n" +
            "cUNBQ2oxQ25wZjhZS00weGlMZUpPWkxmV2FOajRoMUNaNDdmUTBWY1psLy9oVnBOeTMxVEYycVNZ\n" +
            "TGVlVXh5VlpvUUFYMHVJc1gySy9XOG8xOW1FQzBuSGNXZ09JVEw1b0RyV0R2N0tBZGxJcUkwTEJO\n" +
            "WFVkb3RaTDVLUWNvQ1BMMXRaM2JmSnNxR3Q1RVVzc2RxRXNFRjk3YUhBZkREMUdtMEZJWEtHN0pm\n" +
            "QWVSQjRwMHprOTh2dWM1ZzRRZTdqS2ZIaUh0ZVpMb0pLZUVXNFdLeWpEQWo4TWsxd2MxNHU2TjEr\n" +
            "dS8xc1YyMEtkVDFxaXBZb1NTa25XTE9rTmUwcHcxVHdCL2lORmsvVFUxUytpMmZyR1paWjU5ek84\n" +
            "WUw5bFExMWJYeHpRaGlrb3hSaHp6T3NzUjRlT2MwMDRtaE5yZ2ZNclpobkZ1eW5TWXNPaFNJQjUw\n" +
            "a0VvS0tzcmlZSW9NUkZ4eVRLVSs0NWJDU1JtYmVRTzhxTGwwU3hEajUxT3Z3ODlvVVowMXNXR1lL\n" +
            "VDJ2T0JvSFlYNDEvNFptYlZOajJZaVB5KzRPdThOTHZnVWx3Q2p4NjJ2dUpITS84cjU5ckdnb3RN\n" +
            "RVpHRzlYeHJSelM4dz0==";

    static String logRaw = "{\"body\":{\"charset\":\"utf-8\",\"company_name\":\"中国京安进出口公司\",\"ts\":1599126632369,\"v\":\"1.0\"},\"clientAddress\":\"HXBhbAHK1GubVkdpwGHrcmLuWjSNGUuj7STCdnTi67WgvqR4bZ7yfvByvtpGAVERWbc6V43XvtW16qtiAr6TFnqFaBLoB8BM5PnfJGsr2dtoHXW2MYp1EwzmLWVPih7fJoHoSoUKfYC72b\",\"clientPriKey\":\"-----BEGIN PRIVATE KEY-----MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgywB13so0akwcsro7WRvbZMltCAcAKKSrVALVsja5ShCgCgYIKoEcz1UBgi2hRANCAAS1umDbGEKuznBucWnApUwmppsOmHcQu7MTz5sdpQQ1jwGt2NshUmVVKW5T24mGfE3Ni66A+IcD4gBQNHSBtFr2-----END PRIVATE KEY-----\",\"client_sn\":\"1301457541109805056\",\"uri\":\"/intf/risk/getAbnormalInfos\"}";

    public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {

        String rawDataString = "这句话将使用SM4加密";
//        byte[] testKey =  new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};


        Security.addProvider(new BouncyCastleProvider());
        wallet = HXWallet.getInstance();
        wallet.initDefaultInjects();

//        SecretKey secretKey = wallet.generateSM4Key();
//        secretKey.getAlgorithm();
//        byte[] key = secretKey.getEncoded();
//        System.out.println();
//        System.out.println("Algorithm: " + secretKey.getAlgorithm());
//        System.out.println("Format: " +secretKey.getFormat());
//        System.out.println("base64 encoded key:" + Base64.getMimeEncoder().encodeToString(key));
//        System.out.println("Hex encoded Key:" + Hex.toHexString(key));
//        System.out.println("Default IV: " + Hex.toHexString(IV));
//        wallet.updateSM4Cipher(key,IV);
        wallet.updateSM4Cipher(Base64.getDecoder().decode(logKey),IV);

        String encrypt = wallet.encryptBySM4(logRaw);
        System.out.println("encrypt data:" + encrypt);

//        byte[] decrypt = wallet.decryptBySM4(Base64.getMimeDecoder().decode(logEncrpt.replace("\n","")));
//        System.out.println(decrypt);
//        System.out.println();
//        byte[] result = wallet.encryptBySM4(rawDataString.getBytes());
//        System.out.println("Hex encrypt data: " + Hex.toHexString(result));
//
//        byte[] decryptData = wallet.decryptBySM4(result);
//        System.out.println("Hex decrypt data: " + new String(decryptData));
//
//        String encrypt = wallet.encryptBySM4(rawDataString);
//        System.out.println("base64 encoded data: " + encrypt);
//
//        String raw = wallet.decryptBySM4(encrypt);
//        System.out.println("decode base64 data: " + raw);

    }
}
