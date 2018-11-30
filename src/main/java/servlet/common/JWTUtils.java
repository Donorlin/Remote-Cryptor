package servlet.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import database.entity.User;
import database.dao.UserService;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    public static String generateJWT(String username) {
        UserService userService =  new UserService();
        User user = userService.getUserByUsername(username);

        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        c.add(Calendar.MINUTE, 15);
        Date expirationDate = c.getTime();
        Map<String, Object> headerClaims = new HashMap<>();
        headerClaims.put("typ", "JWT");
        headerClaims.put("alg", "HMAC256");

        String token = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(user.getPasswordHash());
            token = JWT.create()
                    .withIssuer("remotecryptor")
                    .withSubject(user.getUsername())
                    .withIssuedAt(now)
                    .withExpiresAt(expirationDate)
                    .withNotBefore(now)
                    .withHeader(headerClaims)
                    .sign(algorithm);

        } catch (JWTCreationException exception) {
            exception.printStackTrace();
        }
        return token;
    }

    public static boolean verifyJWT(String username, String token) {
        UserService userService =  new UserService();
        User user = userService.getUserByUsername(username);

        try {
            Algorithm algorithm = Algorithm.HMAC256(user.getPasswordHash());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("remotecryptor")
                    .withSubject(user.getUsername())
                    .build();
            DecodedJWT jwt = verifier.verify(token);
        } catch (JWTVerificationException exception){
            return false;
        }
        return true;
    }
}
