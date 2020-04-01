import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import xin.heng.HXUtils;
import xin.heng.HXWallet;
import xin.heng.service.HXDefaultHttpClient;
import xin.heng.service.HXService;
import xin.heng.service.IHXHttpClient;
import xin.heng.service.dto.*;
import xin.heng.service.vo.HXBaseUrl;
import xin.heng.service.vo.HXFileHolder;
import xin.heng.service.vo.HXTransaction;
import xin.heng.service.vo.HXTransactionMemo;

import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.UUID;

public class APITest {

    public static HXService api;
    static HXWallet wallet;

    public static void main(String[] args) throws InvalidKeySpecException, InvalidKeyException, SignatureException, IOException, NoSuchProviderException, NoSuchAlgorithmException {
        // 初始化 wallet
        Security.addProvider(new BouncyCastleProvider());
        HXUtils.injectJsonParser(new JsonParser());
        wallet = HXWallet.getInstance();
        wallet.initDefaultInjects();
        wallet.injectSM2Engine(new HXDefaultSM2Engine());
        wallet.setSM2PrivateKey(TestUtil.pemTestKey);

        // 初始化service
        api = new HXService();
        IHXHttpClient client = new HXDefaultHttpClient();
        client.setBaseUrl(new HXBaseUrl("http", "106.14.59.4", 8930));
        api.injectHttpClient(client);
        api.injectWallet(wallet);

        // getInfo
        HXResponse<HXUserInfoBody> response = api.getInfo(TestUtil.userAddress);
        TestUtil.printResult(response);

        // getSnapshots
        HXResponse<HXSnapshotsBody> snapshotsResponse = api.getSnapshots(TestUtil.userAddress, new HXSnapshotRequest());
        TestUtil.printResult(snapshotsResponse);

        // postTransaction 只上传数据，不附带文件
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
        TestUtil.printResult(HXResponse);


        // postTransaction 上传数据并附带文件
        File file = new File("./outputs/hxwallet-1.0.jar");
        HXFileHolder fileHolder = new HXFileHolder()
                .setFile(file)
                .setUploadName("hxwallet.jar");

        HXTransactionMemo fileMemo = new HXTransactionMemo()
                .setT("test-fileupload-type")
                .setD("test-fileData-hxwalletJar")
                .setH(Hex.toHexString(HXWallet.getInstance().digestBySM3("test-fileData-hxwalletJar".getBytes())));

        HXTransactionRequest fileRequestMap = new HXTransactionRequest()
                .setAsset("6b4d1e14ea651021fa5720b9b6e540fcc048760733bc1b0c8756eb84af40f0fa")
                .setMemo(fileMemo)
                .setOpponent_addresses(Collections.singletonList(TestUtil.opponentAddress))
                .setTrace_id(UUID.randomUUID().toString());

        HXResponse<HXResponseBody<HXTransaction>> fileResponse = api.postTransactions(TestUtil.userAddress, fileRequestMap, fileHolder);
        TestUtil.printResult(fileResponse);

    }

}
