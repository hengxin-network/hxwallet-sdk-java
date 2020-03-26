import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import xin.heng.HXUtils;
import xin.heng.HXWallet;
import xin.heng.service.HXDefaultHttpClient;
import xin.heng.service.HXService;
import xin.heng.service.IHXHttpClient;
import xin.heng.service.dto.*;
import xin.heng.service.vo.HXBaseUrl;
import xin.heng.service.vo.HXTransaction;
import xin.heng.service.vo.HXTransactionMemo;

import java.net.MalformedURLException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class APITest {

    public static HXService api;
    static HXWallet wallet;
    public static void main(String[] args) throws InvalidKeySpecException, InvalidKeyException, SignatureException, MalformedURLException, NoSuchProviderException, NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());
        HXUtils.injectJsonParser(new JsonParser());
        wallet = HXWallet.getInstance();
        wallet.initDefaultInjects();
        wallet.injectSM2Engine(new HXDefaultSM2Engine());
        wallet.setSM2PrivateKey(TestUtil.pemTestKey);

        api = new HXService();
        IHXHttpClient client = new HXDefaultHttpClient();
        client.setBaseUrl(new HXBaseUrl("http", "106.14.59.4", 8930));
        api.injectHttpClient(client);
        api.injectWallet(wallet);

        HXResponse<HXUserInfoBody> response = api.getInfo(TestUtil.userAddress);
        printResult(response);

        HXResponse<HXSnapshotsBody> snapshotsResponse = api.getSnapshots(TestUtil.userAddress, new HXSnapshotRequest());
        printResult(snapshotsResponse);

        HXTransactionMemo memo = new HXTransactionMemo()
                .setT("test-type")
                .setD("test-data")
                .setH(Hex.toHexString(HXWallet.getInstance().digestBySM3("test-data".getBytes())));

        HXTransactionRequest requestMap = new HXTransactionRequest()
                .setAsset("6b4d1e14ea651021fa5720b9b6e540fcc048760733bc1b0c8756eb84af40f0fa")
                .setMemo(memo)
                .setOpponent_addresses(Collections.singletonList(TestUtil.opponentAddress))
                .setTrace_id(UUID.randomUUID().toString());
        HXResponse<HXResponseBody<HXTransaction>> HXResponse = api.postTransactions(TestUtil.userAddress, requestMap);
        printResult(HXResponse);
    }

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
