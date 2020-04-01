package xin.heng.sample;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import xin.heng.HXUtils;
import xin.heng.HXWallet;
import xin.heng.sample.business.GetInfoSample;
import xin.heng.sample.business.GetSnapshotsSample;
import xin.heng.sample.business.PostTransactionSample;
import xin.heng.sample.crypto.JWTTokenSample;
import xin.heng.sample.crypto.SM2Sample;
import xin.heng.sample.crypto.SM3Sample;
import xin.heng.sample.crypto.SM4Sample;
import xin.heng.sample.injects.HXDefaultSM2Engine;
import xin.heng.service.HXService;
import xin.heng.service.vo.HXBaseUrl;
import xin.heng.service.vo.HXJwt;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class WalletSample {


    public static void main(String[] args) {

        // 首先需要进行一些初始化工作，sample这里使用了BC进行加密
        // 而 SDK 需要对钱包和工具类进行初始化工作，如果使用BC库，钱包进行default注入即可
        // 工具类中注入的是JSON解析的具体实现
        HXWallet wallet = HXWallet.getInstance();
        HXUtils.injectJsonParser(FastJsonParser.getInstance());
        try {
            Security.addProvider(new BouncyCastleProvider());
            wallet.initDefaultInjects();
            wallet.injectSM2Engine(new HXDefaultSM2Engine());
            // 如要手动注入 wallet 内各个的模块:
//        wallet.injectSM2Signer(new HXDefaultSM2Signer());
//        wallet.injectSM2Engine(new HXDefaultSM2Engine());
//        wallet.injectSM3(new HXDefaultSM3Digest());
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // 获取 sm3 摘要信息
        byte[] sm3digest = SM3Sample.digest("this is a test data");

        // 设置 sm2私钥/公钥和sm4 key
        try {
            wallet.setSM2PrivateKey(SampleUtils.testPrivateKey);
            wallet.setSM2SignerPublicKey(SampleUtils.testPublicKey);
            wallet.updateSM4Cipher(Base64.getMimeDecoder().decode(SampleUtils.sm4TestKeyBase64));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        // 获取 sm2 签名信息
        byte[] sm2Sign = SM2Sample.getSm2Sign("this is test data");

        // sm2 加密
        String rawString = "this will be encrypt by sm2.";
        byte[] sm2result = SM2Sample.encrypt(rawString.getBytes(), SampleUtils.testPublicKey);
        // sm2 解密
        byte[] decryptsm2 = SM2Sample.decrypt(sm2result);

        System.out.println("raw encrypt string: " + rawString);

        // sm4加密数据
        String sm4RawData = "this data will be encrypt by sm4";
        byte[] sm4Data = SM4Sample.encrypt(sm4RawData.getBytes());
        // sm4解密数据
        byte[] decrypt = SM4Sample.decrypt(sm4Data);
        System.out.println("decrypt string: " + new String(decrypt));
        System.out.println("sm4raw string:  " + sm4RawData);

        // 生成 jwt
        HXJwt jwt = JWTTokenSample.generate(SampleUtils.userAddress);

        // 需要指定注入定制的wallet和client时
        // HXDefaultHttpClient client = new HXDefaultHttpClient();
        // HXService service = new HXService(wallet,client);

        // 而service不需要指定传入的wallet时
        // HXService会默认使用 HengxinWallet.getInstance() 的单例实例
        HXService service = new HXService();
        service.setBaseUrl(new HXBaseUrl("http", "106.14.59.4", 8930));

        // getInfo 接口，具体调用查看 GetInfoSample 内
        GetInfoSample.getInfo(service, SampleUtils.userAddress);

        // getSnapshots 接口，具体调用查看 GetSnapshotsSample 内
        GetSnapshotsSample.getSnapshots(service, SampleUtils.userAddress);

        // postTransaction 接口，具体调用查看 PostTransactionSample 内
        PostTransactionSample.postTransaction(service, SampleUtils.userAddress, SampleUtils.opponentAddress, SampleUtils.testAsset);
        PostTransactionSample.postTransactionWithFile(service, SampleUtils.userAddress, SampleUtils.opponentAddress, SampleUtils.testAsset);
    }
}
