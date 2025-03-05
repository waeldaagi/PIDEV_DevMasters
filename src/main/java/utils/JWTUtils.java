package utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import models.User;
import models.UserRole;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    private static final String SECRET = "MySuperSecretKeyForHS256Algorithm12345";
    private static final Key SECRET_KEY = new SecretKeySpec(Base64.getDecoder().decode(Base64.getEncoder().encodeToString(SECRET.getBytes())), SignatureAlgorithm.HS256.getJcaName());

    private static final long ACCESS_TOKEN_EXPIRATION = 30 * 60 * 1000; // 30 minutes
    private static final long REFRESH_TOKEN_EXPIRATION = 60 * 60 * 1000; // 1 hour

    public static String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .claim("phoneNumber", user.getPhoneNumber())
                .claim("address", user.getAddress())
                .claim("birthDate", user.getBirthDate())
                .claim("salary", user.getSalary())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .claim("role", user.getRole().name())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean isTokenValid(String token) {
        try {
            Claims claims = parseToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static User validateAccessToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return new User(
                    (Integer) claims.get("id"),
                    claims.get("username", String.class),
                    claims.get("email", String.class),
                    UserRole.valueOf(claims.get("role", String.class)),
                    true,
                    claims.get("phoneNumber", String.class),
                    claims.get("address", String.class),
                    claims.get("birthDate", Date.class),
                    claims.get("salary", Double.class),
                    null // Password is not included in token
            );
        } catch (JwtException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (JwtException e) {
            e.printStackTrace();
            return false;
        }
    }
}
