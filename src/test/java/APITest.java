import org.bouncycastle.jce.provider.BouncyCastleProvider;
import xin.heng.HXUtils;
import xin.heng.HXWallet;
import xin.heng.service.HXDefaultHttpClient;
import xin.heng.service.HXService;
import xin.heng.service.IHXHttpClient;
import xin.heng.service.dto.*;
import xin.heng.service.vo.*;

import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
        HXResponse<HXUserInfoBody> postUsersResponse = api.postUsers(TestUtil.userAddress);
        TestUtil.printResult(postUsersResponse);

        // getInfo
        HXResponse<HXUserInfoBody> getInfoResponse = api.getInfo(TestUtil.userAddress);
        TestUtil.printResult(getInfoResponse);

        // getSnapshots
        HXSnapshotRequest snapshotRequest = new HXSnapshotRequest()
                .setOrder(HXSnapshotRequest.ORDER_DESC);
        HXResponse<HXSnapshotsBody> snapshotsResponse = api.getSnapshots(TestUtil.userAddress, snapshotRequest);
        TestUtil.printResult(snapshotsResponse);

        // postTransaction 只上传数据，不附带文件
        HXPubData pubData = new HXPubData()
                .setT("test-type")
                .setD("test-data")
                .setH(HXWallet.getInstance().digestBySM3("test-data"));

        HXTransactionRequest requestMap = new HXTransactionRequest()
                .setAsset("6b4d1e14ea651021fa5720b9b6e540fcc048760733bc1b0c8756eb84af40f0fa")
                .setPub_data(pubData)
                .setOpponent_addresses(Collections.singletonList(TestUtil.opponentAddress))

                .setTrace_id(UUID.randomUUID().toString());
        HXResponse<HXResponseBody<HXTransaction>> HXResponse = api.postTransactions(TestUtil.userAddress, requestMap, null);
        TestUtil.printResult(HXResponse);

        // postTransaction 给新的Address附加权限
        List<HXFileInfo> fileInfos;
        if (!snapshotsResponse.responseBody.data.isEmpty() && snapshotsResponse.responseBody.data.get(0).getFiles()!=null && !snapshotsResponse.responseBody.data.get(0).getFiles().isEmpty()){
            fileInfos = snapshotsResponse.responseBody.data.get(0).getFiles();
        }else {
            fileInfos = HXUtils.optFromJson(TestUtil.testFileInfos, HXFileInfoList.class);
        }

        HXTransactionRequest updateFilesRequest = new HXTransactionRequest()
                .setAsset("6b4d1e14ea651021fa5720b9b6e540fcc048760733bc1b0c8756eb84af40f0fa")
                .setPub_data(pubData)
                .setFiles(fileInfos)
                .setOpponent_addresses(Arrays.asList(TestUtil.opponentAddress,TestUtil.userAddress))
                .setTrace_id(UUID.randomUUID().toString());
        HXResponse<HXResponseBody<HXTransaction>> updateFilesResponse = api.postTransactions(TestUtil.userAddress, updateFilesRequest, null);
        TestUtil.printResult(updateFilesResponse);

        // postTransaction 上传数据并附带文件
        File file = new File("./outputs/hxwallet-1.0.jar");
        HXFileHolder fileHolder = new HXFileHolder()
                .setFile(file)
                .setUploadName("hxwallet.jar");

        HXPubData filePubData = new HXPubData()
                .setT("test-fileupload-type")
                .setD("test-fileData-hxwalletJar")
                .setH(HXWallet.getInstance().digestBySM3("test-fileData-hxwalletJar"));

        HXTransactionRequest fileRequestMap = new HXTransactionRequest()
                .setAsset("6b4d1e14ea651021fa5720b9b6e540fcc048760733bc1b0c8756eb84af40f0fa")
                .setPub_data(filePubData)
                .setOpponent_addresses(Collections.singletonList(TestUtil.opponentAddress))
                .setTrace_id(UUID.randomUUID().toString());

        HXResponse<HXResponseBody<HXTransaction>> fileResponse = api.postTransactions(TestUtil.userAddress, fileRequestMap, fileHolder);
        TestUtil.printResult(fileResponse);



    }

}
