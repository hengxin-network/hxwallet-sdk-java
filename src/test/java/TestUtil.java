import org.bouncycastle.util.Strings;
import xin.heng.HXUtils;
import xin.heng.HXWallet;
import xin.heng.service.dto.HXResponse;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
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

    public static String testData = "BH8awtJVUokVfB3qeHlef5CsLpSd8RjTZd5LI66XbOBEFUB/OWUCv5BBdbG6PowjQHXD3E16uo8zZWKzaqYswhq3Qt7TUfOj4R+MeZUdi2ucl4AWyjVYam883rM4OUxiXx1ys2a0m1RImYAW/4bJcu8=";
    public static String tpk = "HX2StGQezPPPRueENiWDywyjx8NQ4TiRFgSjL5v1ZBboCjAnKHrFEQWT7bkdA4G2L1qQmw5n7NjB1PJEwTB4vy6gkrjWcbJQL5Zyvu5F2oJs4sK4E57AEPnmZMbxyoWDjrZSrBnVgiAUXPVA64KH2vX99zWqhEPKNCeTATRXf5D3UzNkeZwawiNtfcURMcQBBER9hCH2B31kxDKoP8DvNZEqi3uB";
    public static String testAddress = "HX9nwFa8rZyZxeJxPAtugKewStDoFn143KPkQXFtRpMKXevMwMfkadDctz8iCdc5YFdxn9ZakFdYnETHSESRdr4EGQ1Z9EqQpdf4Fth4Ef6aiPhBtqkUhQvJ1s98abpTgBTDkCbZW1YE9Gs4pisT7Yut47hwjgVvMLortBp8y135GFeHnyZivMNXVFgjmSJYtRyLwmN5zqvneZN1YhgRNg1A27q";
    public static String testAddressPK  = "-----BEGIN PUBLIC KEY-----\n" +
            "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEkR5CsqJXaj5Bwh3s6X9MG8PxudaZ\n" +
            "nJo86A9QfFHMHQ1OnUrP2siRuQpTzSQkhAi1Fh5DBAITg+m3YZbM4/CbRw==\n" +
            "-----END PUBLIC KEY-----";

    public static String testPrivateKey = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgCpqUlh6/gV6UCMzS\n" +
            "AlE5NDcSlwgtes63Rjuy6aHmkrigCgYIKoEcz1UBgi2hRANCAASk0+Lxu4auOzBc\n" +
            "NEji+J1ZNAYRpdftmeWhESh/REr9PsckVOWRoFBAqgHUnZGTxzCO1WRyTV1qMnRJ\n" +
            "CwlQSebd\n";

    public static String testPublicKey = "-----BEGIN PUBLIC KEY-----\n" +
            "    MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEeDZqNmfR9KZ4Q6wNXFNUBWO7sybV\n" +
            "    N/8MWbyAhFIb3BPSOATFgWQHmEPVr7VYk1OL8fdLD2nfWGc0gtA1OP91uQ==\n" +
            "    -----END PUBLIC KEY-----";

    public static String testPublicKey2 = "-----BEGIN PUBLIC KEY-----\n" +
            "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAESVltMs5lwWWngmIDAUvKrtyNseXC\n" +
            "3vZnq8KW8CU5e5I+dlvQLSoelABXIN2TBnjKvR76kKWwc5j5BtYYOkF9hA==\n" +
            "-----END PUBLIC KEY-----";

    static String userAddress = "HX9nwFa8rZyZxeJxPAtugKewStDoFn143KPkQXFtRpMKXevMwMfkadDctz8iCdc5YFdxn9ZakFdYnETHSESRdr4EGQ1Z9EqQpdf4Fth4Ef6aiPhBtqkUhQvJ1s98abpTgBTDkCbZW1YE9Gs4pisT7Yut47hwjgVvMLortBp8y135GFeHnyZivMNXVFgjmSJYtRyLwmN5zqvneZN1YhgRNg1A27q";

    static String opponentAddress = "HX2DKYCAp2b36THYMKdVsM8KUEF7m92aF5MkTMWFN7ex7xttYusHRwy19kYGjZCPxYDezgZqPgpKLSFMMFtbAHFD41nEKZW9EPUQLZQe1uhp7bU4WC8PN4jvKe2jGdxd4KtdpxnmG8TJemCXqbgjPanQxL6BEJABFPBDERgPh6WvswpNdiZCDNMbTYVVAMv3izgHumSr622Gvz8xn82rRkYQaFFn";

    // test环境 height 174 的文件
    static String testFileInfos = "[{\"dp\":0,\"h\":\"a33799cd1f4647b94845ba5cf765f4d2b4e79f0808afd867d6210ba915a95ecb\",\"rh\":\"faa45a10986724bd36cc3cd8ba77ea59016a63484154fbd300060d3439005a51\",\"s\":48}]";

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
