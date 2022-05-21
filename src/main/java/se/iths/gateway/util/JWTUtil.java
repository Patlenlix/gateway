package se.iths.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class JWTUtil {

    @Value("${key.public:secret-key}")
    private String encodedKey;

    public Jws<Claims> getAllClaimsFromToken(String authToken) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String base64EncodedKey = encodedKey;

        byte[] byteKey = Base64.getDecoder().decode(base64EncodedKey.getBytes());
        X509EncodedKeySpec x509PublicKey = new X509EncodedKeySpec(byteKey);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509PublicKey);

        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(authToken);
    }
}
