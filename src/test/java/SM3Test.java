import org.bouncycastle.jce.provider.BouncyCastleProvider;
import xin.heng.HXWallet;
import xin.heng.service.error.InvalidAddressException;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class SM3Test {

    public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAddressException {
        Security.addProvider(new BouncyCastleProvider());
        TestUtil.initInjectsByDefault();
//        String message = "eyJhbGciOiJTTTIiLCJ0eXAiOiJKV1QifQ.eyJleHAiOjE1ODQwOTUzNjIsImlhdCI6MTU4NDA4ODE2MiwiaXNzIjoiSFh0TWVaNG5NYXI3aE13YnB3UDJFTFpyaEpkWU1VdlgxcmZERzdoaDhwaHdzVDNDOWt0NGZaTWphN3Y3d25rNkdRcVZTUGhCYVl4ZktUVnNCWDdBcEwzRjVGd0xYMkphWHE1eHJ2UGRXdGg2REpkOTE3dzJrUEVxb2lwZFdTa0NNQUpkaGYzQXNtVmZhTWtxMzJiUnB4NXFzUDJtRHlWZWh6MXNHUVV5a2R5Qm9BNXQ5d3NpN0xvSEJ6OXpKSzNENHo3V3A4aXdaaXZ1NUpEa1hDMTRkZjd5Znc5IiwibmMiOiIwODdiYzg1MS0yNzhmLTRlZjYtYWUyYS01Yzk4MTFkZTQyOTQifQ";
//        String message = "这是一条测试数据";
//        String result = HXWallet.getInstance().digestBySM3(message);
//        byte[] bytesResult = HXWallet.getInstance().digestBySM3(message.getBytes());
//        System.out.println(Base64.getMimeEncoder().encodeToString(bytesResult));
//        System.out.println(result);


        byte[] decodePk = HXWallet.getInstance().decodeAddress(TestUtil.testAddress);
        // 创建给KeyFactory使用的KeySpec
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decodePk);

        // 验证解析出的address和pem格式的address是否一致
        String s = Base64.getMimeEncoder().encodeToString(decodePk)
                // 去掉换行符的干扰
                .replace("\r\n", "")
                .replace("\n", "");
        System.out.println(Base64.getMimeEncoder().encodeToString(decodePk));
        String addressPk = TestUtil.testAddressPK;
        // 去除私钥的PEM格式以及换行符的干扰
        addressPk = addressPk.replace("-----BEGIN PUBLIC KEY-----", "");
        addressPk = addressPk.replace("-----END PUBLIC KEY-----", "");
        byte[] addressPkBytes = Base64.getMimeDecoder().decode(addressPk);
        addressPk = addressPk.replace("\r\n", "");
        addressPk = addressPk.replace("\n", "");

        System.out.println(addressPk);
        System.out.println("equals:" + addressPk.contentEquals(s));
        // 不去除换行符干扰，直接使用byte[]进行对比比较
        System.out.println("byte[] equals:"+Arrays.equals(decodePk,addressPkBytes));

    }
}
