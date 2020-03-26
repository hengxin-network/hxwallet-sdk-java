package xin.heng.sample.crypto;

import xin.heng.HXUtils;
import xin.heng.HXWallet;
import xin.heng.service.vo.HXJwt;
import xin.heng.service.vo.HXJwtBuildMaterial;

import java.security.SignatureException;

public class JWTTokenSample {

    public static HXJwt generate(String address) {
        try {

            HXJwtBuildMaterial buildMaterial = new HXJwtBuildMaterial();
            buildMaterial.setAddress(address);
            buildMaterial.setBody(null);
            buildMaterial.setUrl(HXUtils.buildUrlPathWithQueries("/info", null));
            buildMaterial.setRequestMethod("GET");

            HXJwt jwt = HXUtils.buildJwt(HXWallet.getInstance(), buildMaterial);
            System.out.println(HXUtils.optToJson(jwt));
            return jwt;
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }
}
