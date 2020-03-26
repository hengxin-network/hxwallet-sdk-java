//package xin.heng.crypto;
//
//import org.bouncycastle.crypto.InvalidCipherTextException;
//import org.bouncycastle.crypto.engines.SM2Engine;
//import org.bouncycastle.crypto.params.ECDomainParameters;
//import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
//import org.bouncycastle.crypto.params.ECPublicKeyParameters;
//import org.bouncycastle.crypto.params.ParametersWithRandom;
//import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
//import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
//import org.bouncycastle.jce.spec.ECParameterSpec;
//
//import java.security.*;
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.util.Base64;
//
//public class HXDefaultSM2Engine implements IHXSM2Engine {
//
//    private SM2Engine encryptEngine = new SM2Engine(SM2Engine.Mode.C1C3C2);
//    private SM2Engine decryptEngine = new SM2Engine(SM2Engine.Mode.C1C3C2);
//
//    private IHXKeyFactory keyFactory;
//
//    @Override
//    public byte[] encrypt(byte[] data, PublicKey key) {
//        ECPublicKeyParameters ecPublicKeyParameters = null;
//        if (key instanceof BCECPublicKey) {
//            BCECPublicKey publicKey = (BCECPublicKey) key;
//            ECParameterSpec s = publicKey.getParameters();
//            ECDomainParameters domainParameters = new ECDomainParameters(s.getCurve(), s.getG(), s.getN());
//            ecPublicKeyParameters = new ECPublicKeyParameters(publicKey.getQ(), domainParameters);
//        }
//        encryptEngine.init(true, new ParametersWithRandom(ecPublicKeyParameters, new SecureRandom()));
//        byte[] result;
//
//        try {
//            result = encryptEngine.processBlock(data, 0, data.length);
//            return result;
//        } catch (InvalidCipherTextException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @Override
//    public byte[] decrypt(byte[] data) {
//        byte[] result;
//        try {
//            result = decryptEngine.processBlock(data, 0, data.length);
//            return result;
//        } catch (InvalidCipherTextException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @Override
//    public void setPrivateKey(PrivateKey key) {
//        ECPrivateKeyParameters ecPrivateKeyParameters = null;
//        if (key instanceof BCECPrivateKey) {
//            BCECPrivateKey privateKey = (BCECPrivateKey) key;
//            ECParameterSpec s = privateKey.getParameters();
//            ECDomainParameters domainParameters = new ECDomainParameters(s.getCurve(), s.getG(), s.getN());
//            ecPrivateKeyParameters = new ECPrivateKeyParameters(privateKey.getD(), domainParameters);
//        }
//        decryptEngine.init(false, ecPrivateKeyParameters);
//    }
//
//    @Override
//    public void setPrivateKey(String encodedKey) {
//        encodedKey = encodedKey.replace("-----BEGIN PRIVATE KEY-----", "");
//        encodedKey = encodedKey.replace("-----END PRIVATE KEY-----", "");
//        byte[] decode = Base64.getMimeDecoder().decode(encodedKey);
//        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decode);
//        try {
//            if (keyFactory == null) {
//                HXDefaultKeyFactory factory = new HXDefaultKeyFactory();
//                factory.injectDefault();
//                keyFactory = factory;
//            }
//            keyFactory.generatePrivate(privateKeySpec);
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchProviderException e) {
//            e.printStackTrace();
//        }
//    }
//}
