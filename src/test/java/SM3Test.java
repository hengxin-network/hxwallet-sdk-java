import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import xin.heng.HXWallet;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Arrays;

public class SM3Test {

    public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());
        TestUtil.initInjectsByDefault();
        String message = "eyJhbGciOiJTTTIiLCJ0eXAiOiJKV1QifQ.eyJleHAiOjE1ODQwOTUzNjIsImlhdCI6MTU4NDA4ODE2MiwiaXNzIjoiSFh0TWVaNG5NYXI3aE13YnB3UDJFTFpyaEpkWU1VdlgxcmZERzdoaDhwaHdzVDNDOWt0NGZaTWphN3Y3d25rNkdRcVZTUGhCYVl4ZktUVnNCWDdBcEwzRjVGd0xYMkphWHE1eHJ2UGRXdGg2REpkOTE3dzJrUEVxb2lwZFdTa0NNQUpkaGYzQXNtVmZhTWtxMzJiUnB4NXFzUDJtRHlWZWh6MXNHUVV5a2R5Qm9BNXQ5d3NpN0xvSEJ6OXpKSzNENHo3V3A4aXdaaXZ1NUpEa1hDMTRkZjd5Znc5IiwibmMiOiIwODdiYzg1MS0yNzhmLTRlZjYtYWUyYS01Yzk4MTFkZTQyOTQifQ";
        byte[] result = HXWallet.getInstance().digestBySM3(message.getBytes());
        System.out.println("length: " + result.length);
        System.out.println(Arrays.toString(result));
        String stringResult = Hex.toHexString(result);
        System.out.println(stringResult);
    }
}
