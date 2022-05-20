package se.iths.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class JWTUtil {

    public Jws<Claims> getAllClaimsFromToken(String authToken) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String base64EncodedKey = getBase64EncodedKey();

        byte[] byteKey = Base64.getDecoder().decode(base64EncodedKey.getBytes());
        X509EncodedKeySpec x509PublicKey = new X509EncodedKeySpec(byteKey);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509PublicKey);

        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(authToken);
    }

    private String getBase64EncodedKey() {
        return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxRDTrtKTqzK/Muw9A34JrnBYHQoMp75oPktLLzHRA21of03OTcykLM2tm0EZ9uKDbvA0PMmA16FEYO2dla35BSzxUt2wmhdOCMipi73Jkafj54U2dwW5wxvYN1WOedOAE+7/PnCa9DYTSg8NmOYoKZcZf0UUGCxcIIWRbvmaUUw8yFmcpfou2rTEtA+uKabn4Hc1j6OR035yq8UwasqxmJ9VcOxVeDenF1iV3BGxvan7zzR9I9Npt7Q2WggC7fz9rxYbnpigu1HkJOzR5GWijjjGa4xR/o2rgHZHaix8oP+rQbtJD9qmckY7DZgRaVq8yaYxFb2Q1fq0VIuJAgLz+QIDAQAB";
    }
}
