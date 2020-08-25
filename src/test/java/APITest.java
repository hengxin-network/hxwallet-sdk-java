import kotlin.random.Random;
import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.util.encoders.Hex;
import xin.heng.HXUtils;
import xin.heng.HXWallet;
import xin.heng.service.HXDefaultHttpClient;
import xin.heng.service.HXService;
import xin.heng.service.IHXHttpClient;
import xin.heng.service.dto.*;
import xin.heng.service.vo.HXBaseUrl;
import xin.heng.service.vo.HXPubData;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

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

        X9ECParameters sm2 = GMNamedCurves.getByName("sm2p256v1");
        ECParameterSpec sm2Spec = new ECParameterSpec(sm2.getCurve(), sm2.getG(), sm2.getN());

        byte[] pub = Hex.decode("02b966d6418f74bc9eedac72531add0e4417f3accf38f49479a3ac76f5ceb72896");
        final X9ECParameters x9ECParameters = ECUtil.getNamedCurveByName("sm2p256v1");
        final ECCurve curve = x9ECParameters.getCurve();
        final ECPoint point = EC5Util.convertPoint(curve.decodePoint(pub));

        // 根据曲线恢复公钥格式
        final ECNamedCurveSpec ecSpec = new ECNamedCurveSpec("sm2p256v1", curve, x9ECParameters.getG(), x9ECParameters.getN());

        byte[] decode = Hex.decode("00aa0fd187cbdc8517e060949f9881ff56fd8001454d7abc8fd1775e3a2f7f49e5");
        ECPrivateKeySpec keySpec = new ECPrivateKeySpec(new BigInteger(1, decode), sm2Spec);
        KeyFactory kf = KeyFactory.getInstance("EC", "BC");
        BCECPrivateKey privateKey = (BCECPrivateKey) kf.generatePrivate(keySpec);
        BCECPublicKey publicKey = (BCECPublicKey) kf.generatePublic(new ECPublicKeySpec(point, ecSpec));
        wallet.setSM2PrivateKey(privateKey);
        wallet.setSM2SignerPublicKey(publicKey);

        // 初始化service
        api = new HXService();
        IHXHttpClient client = new HXDefaultHttpClient();
        client.setBaseUrl(new HXBaseUrl("https", "hxwallet.testnet.heng.xin"));
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
        TestCard testCard = new TestCard()
                .setName("test-card")
                .setNumber(String.valueOf(Random.Default.nextInt()));
//
        HXPubData<TestCard> pubData = new HXPubData<TestCard>()
                .setT("test-type")
                .setD(testCard);
//
        Map<String, Object> map = new HashMap<>();
        map.put("test", "private_test");
//
        HXTransactionRequest requestMap = new HXTransactionRequest()
                .setAsset("2fc7e3f2a98476d0b8de31b549474c4340cd55b3d040cca8391d710aef893a93")
                .setPub_data(pubData)
                .setPriv_data(map)
                .setSenders_required(false)
                .setReceivers_required(true)
                .setAsync(true)
                .setOpponent_addresses(Collections.singletonList(TestUtil.userAddress))
                .setTrace_id(UUID.randomUUID().toString());
        System.out.println("=======Post Transaction===========");
        HXResponse<HXSnapshotBody> HXResponse = api.postTransactions(TestUtil.userAddress, requestMap, null);
        TestUtil.printResult(HXResponse);

////        // postTransaction 给新的Address附加权限
//        List<HXFileInfo> fileInfos = new ArrayList<>();
//        if (!snapshotsResponse.responseBody.data.isEmpty() && snapshotsResponse.responseBody.data.get(0).getFiles() != null && !snapshotsResponse.responseBody.data.get(0).getFiles().isEmpty()) {
//            fileInfos = snapshotsResponse.responseBody.data.get(0).getFiles();
//        }
//
//        HXTransactionRequest updateFilesRequest = new HXTransactionRequest()
//                .setAsset("2fc7e3f2a98476d0b8de31b549474c4340cd55b3d040cca8391d710aef893a93")
//                .setPub_data(pubData)
//                .setFiles(fileInfos)
//                .setOpponent_addresses(Arrays.asList(TestUtil.userAddress, TestUtil.userAddress))
//                .setTrace_id(UUID.randomUUID().toString());
//        System.out.println("POST /transaction/ 给文件追加一个新的address的访问权限");
//        HXResponse<HXSnapshotBody> updateFilesResponse = api.postTransactions(TestUtil.userAddress, updateFilesRequest, null);
//        TestUtil.printResult(updateFilesResponse);
//
////        // postTransaction 上传数据并附带文件
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
//                .setAsset("2fc7e3f2a98476d0b8de31b549474c4340cd55b3d040cca8391d710aef893a93")
//                .setPub_data(filePubData)
//                .setOpponent_addresses(Collections.singletonList(TestUtil.userAddress))
//                .setTrace_id(UUID.randomUUID().toString());
//
//        HXResponse<HXSnapshotBody> fileResponse = api.postTransactions(TestUtil.userAddress, fileRequestMap, fileHolder);
//        System.out.println("POST /transaction/ 上传文件");
//        TestUtil.printResult(fileResponse);
//
//        // snapshot by id
//        long snapshotId = 28;
//        HXResponse<HXSnapshotBody> snapshotResponse = api.getSnapshot(TestUtil.userAddress, snapshotId);
//        System.out.println("GET /snapshots/" + snapshotId);
//        TestUtil.printResult(snapshotResponse);
//
////        // 从链上获取并下载文件保存到本地
//        File downloadFile = new File("./download/test_" + System.currentTimeMillis() + ".txt");
//        if (!downloadFile.exists()) {
//            File parent = new File(downloadFile.getParent());
//            if (!parent.exists()) parent.mkdirs();
//            downloadFile.createNewFile();
//        }
//        HXResponse<File> fileHXResponse = api.getFile(TestUtil.userAddress, (HXFileInfo) snapshotResponse.responseBody.data.getFiles().get(0), downloadFile);
//        // 文件已经被下载到了downloadFile所对应文件，或者可以也通过response里的responseBody获取到file对象
//        File resultFile = fileHXResponse.responseBody;

        // snapshot by hash
        String txHash = "35355b32a533b27cd78264b8c62ced3a37eaf1269e16f2277c9891ce17875ae0";
        HXResponse<HXSnapshotBody> snapshotByTxHash = api.getSnapshotByTxHash(TestUtil.userAddress, txHash);
        System.out.println("GET /snapshots/" + txHash);
        TestUtil.printResult(snapshotByTxHash);

        // network snapshots
        ArrayList<String> addrs = new ArrayList<>();
        addrs.add(TestUtil.userAddress);
        HXNetworkSnapshotsRequest request = new HXNetworkSnapshotsRequest()
                .setAddresses(addrs);
        xin.heng.service.dto.HXResponse<HXSnapshotsBody> response = api.getNetworkSnapshots(TestUtil.userAddress, request);
        System.out.println("GET /network/snapshots");
        TestUtil.printResult(response);
    }


}
