import kotlin.random.Random;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import xin.heng.HXUtils;
import xin.heng.HXWallet;
import xin.heng.service.HXDefaultHttpClient;
import xin.heng.service.HXService;
import xin.heng.service.IHXHttpClient;
import xin.heng.service.dto.*;
import xin.heng.service.vo.HXBaseUrl;
import xin.heng.service.vo.HXPubData;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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

        // postUser
//        HXResponse<HXUserInfoBody> postUsersResponse = api.postUsers(TestUtil.userAddress);
//        TestUtil.printResult(postUsersResponse);

        // getInfo
//        HXResponse<HXUserInfoBody> getInfoResponse = api.getInfo(TestUtil.userAddress);
//        TestUtil.printResult(getInfoResponse);

        // getSnapshots
        HXSnapshotRequest snapshotRequest = new HXSnapshotRequest()
                .setOrder(HXSnapshotRequest.ORDER_DESC);
        HXResponse<HXSnapshotsBody> snapshotsResponse = api.getSnapshots(TestUtil.userAddress, snapshotRequest);
        TestUtil.printResult(snapshotsResponse);

        // postTransaction 只上传数据，不附带文件
        TestCard testCard = new TestCard()
                .setName("test-card")
                .setNumber(String.valueOf(Random.Default.nextInt()));

        HXPubData<TestCard> pubData = new HXPubData<TestCard>()
                .setT("test-type")
                .setD(testCard);

        Map<String, Object> map = new HashMap<>();
        map.put("test", "private_test");

        HXTransactionRequest requestMap = new HXTransactionRequest()
                .setAsset("6b4d1e14ea651021fa5720b9b6e540fcc048760733bc1b0c8756eb84af40f0fa")
                .setPub_data(pubData)
                .setPriv_data(map)
                .setSenders_required(false)
                .setReceivers_required(true)
                .setOpponent_addresses(Collections.singletonList(TestUtil.opponentAddress))
                .setTrace_id(UUID.randomUUID().toString());

        HXResponse<HXSnapshotBody> HXResponse = api.postTransactions(TestUtil.userAddress, requestMap, null);
        TestUtil.printResult(HXResponse);

//        // postTransaction 给新的Address附加权限
//        List<HXFileInfo> fileInfos;
//        if (!snapshotsResponse.responseBody.data.isEmpty() && snapshotsResponse.responseBody.data.get(0).getFiles() != null && !snapshotsResponse.responseBody.data.get(0).getFiles().isEmpty()) {
//            fileInfos = snapshotsResponse.responseBody.data.get(0).getFiles();
//        } else {
//            fileInfos = JSON.parseArray(TestUtil.testFileInfos, HXFileInfo.class);
//        }
//
//        HXTransactionRequest updateFilesRequest = new HXTransactionRequest()
//                .setAsset("6b4d1e14ea651021fa5720b9b6e540fcc048760733bc1b0c8756eb84af40f0fa")
//                .setPub_data(pubData)
//                .setFiles(fileInfos)
//                .setOpponent_addresses(Arrays.asList(TestUtil.opponentAddress, TestUtil.userAddress))
//                .setTrace_id(UUID.randomUUID().toString());
//        System.out.println("POST /transaction/ 给文件追加一个新的address的访问权限");
//        HXResponse<HXSnapshotBody> updateFilesResponse = api.postTransactions(TestUtil.userAddress, updateFilesRequest, null);
//        TestUtil.printResult(updateFilesResponse);
//
//        // postTransaction 上传数据并附带文件
//        File file = new File("./src/test/resources/test.txt");
//        HXFileHolder fileHolder = new HXFileHolder()
//                .setFile(file)
//                .setUploadName("test.txt");
//
//        HXPubData filePubData = new HXPubData()
//                .setT("test-fileupload-type")
//                .setD("test-fileData-testfile");
//
//        HXTransactionRequest fileRequestMap = new HXTransactionRequest()
//                .setAsset("6b4d1e14ea651021fa5720b9b6e540fcc048760733bc1b0c8756eb84af40f0fa")
//                .setPub_data(filePubData)
//                .setOpponent_addresses(Collections.singletonList(TestUtil.opponentAddress))
//                .setTrace_id(UUID.randomUUID().toString());
//
//        HXResponse<HXSnapshotBody> fileResponse = api.postTransactions(TestUtil.userAddress, fileRequestMap, fileHolder);
//        System.out.println("POST /transaction/ 上传文件");
//        TestUtil.printResult(fileResponse);
//
//        // 从链上获取并下载文件保存到本地
//        File downloadFile = new File("./download/test_" + System.currentTimeMillis() + ".txt");
//        if (!downloadFile.exists()) {
//            File parent = new File(downloadFile.getParent());
//            if (!parent.exists()) parent.mkdirs();
//            downloadFile.createNewFile();
//        }
//        HXResponse<File> fileHXResponse = api.getFile(TestUtil.userAddress, fileInfos.get(0), downloadFile);
//        // 文件已经被下载到了downloadFile所对应文件，或者可以也通过response里的responseBody获取到file对象
//        File resultFile = fileHXResponse.responseBody;

        // snapshot by id
        long snapshotId = 461;
        HXResponse<HXSnapshotBody> snapshotResponse = api.getSnapshot(TestUtil.userAddress, snapshotId);
        System.out.println("GET /snapshots/" + snapshotId);
        TestUtil.printResult(snapshotResponse);

        // snapshot by hash
        String txHash = "7f68bc626d4a8d27680c310cd0dfe9ac8d224325c7988ccbb705d1c12abc184f";
        HXResponse<HXSnapshotBody> snapshotByTxHash = api.getSnapshotByTxHash(TestUtil.userAddress, txHash);
        System.out.println("GET /snapshots/" + txHash);
        TestUtil.printResult(snapshotByTxHash);
    }


}
