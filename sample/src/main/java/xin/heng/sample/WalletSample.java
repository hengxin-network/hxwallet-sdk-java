package xin.heng.sample;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import xin.heng.HXUtils;
import xin.heng.HXWallet;
import xin.heng.crypto.HXDefaultSM2Signer;
import xin.heng.crypto.HXDefaultSM3Digest;
import xin.heng.sample.business.GetInfoSample;
import xin.heng.sample.business.GetSnapshotsSample;
import xin.heng.sample.business.PostTransactionSample;
import xin.heng.sample.crypto.JWTTokenSample;
import xin.heng.sample.crypto.PrivateKeySample;
import xin.heng.sample.crypto.SM2Sample;
import xin.heng.sample.crypto.SM3Sample;
import xin.heng.sample.injects.HXDefaultSM2Engine;
import xin.heng.service.HXDefaultHttpClient;
import xin.heng.service.HXService;
import xin.heng.service.vo.HXBaseUrl;
import xin.heng.service.vo.HXJwt;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

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

        // 设置 私钥
        PrivateKeySample.update(SampleUtils.pemKey);

        // 获取 sm2 签名信息
        byte[] sm2Sign = SM2Sample.getSm2Sign("this is test data");

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
        PostTransactionSample.postTransaction(service, SampleUtils.userAddress, SampleUtils.opponentAddress);
    }
}
