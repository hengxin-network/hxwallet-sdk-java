import org.bouncycastle.util.Strings;
import xin.heng.HXUtils;
import xin.heng.HXWallet;
import xin.heng.service.dto.HXResponse;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.util.Optional;

public class TestUtil {


    public static void main(String[] args) {
        byte[] b = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
        StringBuilder builder = new StringBuilder();
        System.out.println(Strings.fromUTF8ByteArray(b));
        for (byte b1 : b) {
            builder.append(String.valueOf(b1));
        }
        System.out.println(builder.toString());
    }

    public static void initInjectsByDefault() throws NoSuchProviderException, NoSuchAlgorithmException {
        HXWallet.getInstance().initDefaultInjects();
    }

    public static String pemTestKey = "-----BEGIN PRIVATE KEY-----\n" +
            "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQghwBKUJ/WL+5S8ICX\n" +
            "q4RD/J+Pb4rN/DCKPyvnZUpQf3KgCgYIKoEcz1UBgi2hRANCAAR4Nmo2Z9H0pnhD\n" +
            "rA1cU1QFY7uzJtU3/wxZvICEUhvcE9I4BMWBZAeYQ9WvtViTU4vx90sPad9YZzSC\n" +
            "0DU4/3W5\n" +
            "-----END PRIVATE KEY-----";

    public static String testPublicKey = "-----BEGIN PUBLIC KEY-----\n" +
            "    MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEeDZqNmfR9KZ4Q6wNXFNUBWO7sybV\n" +
            "    N/8MWbyAhFIb3BPSOATFgWQHmEPVr7VYk1OL8fdLD2nfWGc0gtA1OP91uQ==\n" +
            "    -----END PUBLIC KEY-----";

    static String userAddress = "HX9nwFa8rZyZxeJxPAtugKewStDoFn143KPkQXFtRpMKXevMwMfkadDctz8iCdc5YFdxn9ZakFdYnETHSESRdr4EGQ1Z9EqQpdf4Fth4Ef6aiPhBtqkUhQvJ1s98abpTgBTDkCbZW1YE9Gs4pisT7Yut47hwjgVvMLortBp8y135GFeHnyZivMNXVFgjmSJYtRyLwmN5zqvneZN1YhgRNg1A27q";

    static String opponentAddress = "HX19vwbnbazqbynReuZfywqLRN7XqVWDwCp4MDDLhjWarWCJ7ic3Lnnm8uLLvXSHJSvtEwywq9Qobn9X8rD6VX73AHMJZFNnmHhhcWQbA69kgtqyrixmSAndwX8RL9tu1HSk7SGwDcRzqcvJWXuwids9p3osiHYFDnirhHW5P6qHBDbfbUnQAmSwqDjg7Sm5oExqH22eEfHNEjzgsCUkfVFbMU6";

    // test环境 height 174 的文件
    static String testFileInfos = "[{\"dp\":0,\"h\":\"327e70426a2618b8fe49c5f9e636fb6f407694685e6f8b268f0c3db744cbb411\",\"rh\":\"3cae0216c88bbeb79dbf61b9668e78ba24b5904156738071d674ddb6a1418222\",\"s\":39312}]";

    public static void printResult(HXResponse response) {
        System.out.println(response.httpCode);
        if (response.isSuccess()) {
            if (response.responseBody instanceof String) {
                System.out.println(response.responseBody);
            } else {
                System.out.println(HXUtils.optToJson(response.responseBody));
            }
        } else {
            if (response.responseBody instanceof String) {
                System.out.println(response.responseBody);
            } else {
                System.out.println(HXUtils.optToJson(response.responseBody));
            }
            Optional.ofNullable(response.originError).ifPresent(Throwable::printStackTrace);
        }
    }
}
