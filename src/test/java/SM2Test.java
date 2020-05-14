import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;
import org.bouncycastle.util.encoders.Hex;
import xin.heng.HXWallet;
import xin.heng.crypto.HXDefaultKeyFactory;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SM2Test {

    public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException, IOException {
        Security.addProvider(new BouncyCastlePQCProvider());
        Security.addProvider(new BouncyCastleProvider());
        HXWallet.getInstance().initDefaultInjects();
        HXWallet.getInstance().injectSM2Engine(new HXDefaultSM2Engine());
        HXDefaultKeyFactory keyFactory = new HXDefaultKeyFactory();
        keyFactory.injectDefault();
        String testData = "haha.c1c2c3";

        String encryptDataHex = "042f854ec1513910f9a63d81dacc7d4d90877e1b165f8b14e8f6c5a843d1b9598bab915dfba5e7c633aeea1a6eb65aba5d0d2ea6a8d4f189213765da23da5163c2e5bee851fec6fc42af2359c16110da759ddb21e217d43897f4cdd7fd70667f066f4bff09";
//        String encryptDataHex = "04f28370a13ea81ff3bc0e79c60957dc9e25d2b61d7815661ccba9ea4660469ca9f5f902b1750e4277caf009df4f66a61cb9879ffa656c50ed6fc8c73914e9a306664332de7cfb8cea1eb3b4bcbbaf2cd2c7e0c83376609ce9331483534385abc2fdff1143";

        HXWallet.getInstance().setSM2PrivateKey(TestUtil.pemTestKey);
        byte[] decrypt = HXWallet.getInstance().decryptBySM2(Hex.decode(encryptDataHex));
        System.out.println(new String(decrypt));
//        byte[] bytes = HXWallet.getInstance().signBySM2(testData.getBytes(StandardCharsets.UTF_8));

//        System.out.println("hex sign:" + Hex.toHexString(bytes));

//        String pk = TestUtil.pemTestKey;
//        pk = pk.replace("-----BEGIN PRIVATE KEY-----", "")
//                .replace("-----END PRIVATE KEY-----","");
//        byte[] decode = Base64.getMimeDecoder().decode(pk);
//        System.out.println("private key: " + Hex.toHexString(decode));

        String encodedKey = TestUtil.testPublicKey;
        encodedKey = encodedKey.replace("-----BEGIN PUBLIC KEY-----", "");
        encodedKey = encodedKey.replace("-----END PUBLIC KEY-----", "");
        byte[] publicDecode = Base64.getMimeDecoder().decode(encodedKey);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicDecode);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        byte[] bytes = HXWallet.getInstance().encryptBySM2(testData.getBytes(), publicKey);
        System.out.println(Hex.toHexString(bytes));

//        System.out.println(Hex.toHexString(publicDecode));
//        String testHexSig = "3045022002d086abb130253644921413f7cb2884e2dc8bfa38915728d88f0dd082b4b0dc022100a452ca6c8144e89bfb5cb110564d2e787a94de39bebbbff5080f966057542b32";
//        String testHexSig = "304502207203de228e033ccf6118ff33f781db302093e1723a2f5fc5853b98791b0290b0022100c74e9035fa5aa19e281b7e6b9643d071695debee008fd96a64d89ef46d72613d";
//        HXWallet.getInstance().setSM2SignerPublicKey(TestUtil.testPublicKey);
//        System.out.println("verify signature result: " + HXWallet.getInstance().verifyBySM2(testData.getBytes(), bytes));
//        System.out.println("verify signature result: " + HXWallet.getInstance().verifyBySM2(testData.getBytes(), Hex.decode(testHexSig)));
//        System.out.println(Hex.decode(testHexSig).length);

//        ASN1Sequence goAsn1Sequence = ASN1Sequence.getInstance(Hex.decode(testHexSig));
//        ASN1Encodable[] encodables = goAsn1Sequence.toArray();
//
//        System.out.println(Hex.toHexString(encodables[0].toASN1Primitive().getEncoded()));
//        System.out.println(Hex.toHexString(encodables[1].toASN1Primitive().getEncoded()));
//
//        ASN1Sequence javaAsn1Sequence = ASN1Sequence.getInstance(bytes);
//        System.out.println(Hex.toHexString(javaAsn1Sequence.toArray()[0].toASN1Primitive().getEncoded()));
//        System.out.println(Hex.toHexString(javaAsn1Sequence.toArray()[1].toASN1Primitive().getEncoded()));

//        try {
//            ASN1InputStream asn1InputStream = new ASN1InputStream(Hex.decode(testHexSig));
//            //将hex转换为byte输出
//            ASN1Primitive asn1Primitive = null;
//            while ((asn1Primitive = asn1InputStream.readObject()) != null) {
//                //循环读取，分类解析。这样的解析方式可能不适合有两个同类的ASN1对象解析，如果遇到同类，那就需要按照顺序来调用readObject，就可以实现解析了。
//                if (asn1Primitive instanceof ASN1Integer) {
//                    ASN1Integer asn1Integer = (ASN1Integer) asn1Primitive;
//                    System.out.println("Integer:" + asn1Integer.getValue());
//                } else if (asn1Primitive instanceof ASN1Boolean) {
//                    ASN1Boolean asn1Boolean = (ASN1Boolean) asn1Primitive;
//                    System.out.println("Boolean:" + asn1Boolean.isTrue());
//                } else if (asn1Primitive instanceof ASN1Sequence) {
//                    ASN1Sequence asn1Sequence = (ASN1Sequence) asn1Primitive;
//                    ASN1SequenceParser asn1SequenceParser = asn1Sequence.parser();
//                    ASN1Encodable asn1Encodable = null;
//                    while ((asn1Encodable = asn1SequenceParser.readObject()) != null) {
//                        asn1Primitive = asn1Encodable.toASN1Primitive();
//                        if (asn1Primitive instanceof ASN1String) {
//                            ASN1String string = (ASN1String) asn1Primitive;
//                            System.out.println("PrintableString:" + string.getString());
//                        } else if (asn1Primitive instanceof ASN1UTCTime) {
//                            ASN1UTCTime asn1utcTime = (ASN1UTCTime) asn1Primitive;
//                            System.out.println("UTCTime:" + asn1utcTime.getTime());
//                        } else if (asn1Primitive instanceof ASN1Null) {
//                            System.out.println("NULL");
//                        } else if (asn1Primitive instanceof ASN1Integer){
//                            System.out.println("Integer: " + ((ASN1Integer) asn1Primitive).getValue().toString());
//                        }
//                    }
//                }
//            }
//    } catch(
//    Exception e)
//
//    {
//        e.printStackTrace();
//    }
    }
}
