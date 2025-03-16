package me.fmroz.auth;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

public class JwtUtil {

    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15; // 15 min
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7 dni

    private static String SECRET_KEY;
    private static Key key;

    static {
        try {
            Dotenv dotenv = Dotenv.load();
            SECRET_KEY = dotenv.get("JWT_SECRET");
            if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
                throw new IllegalStateException("JWT_SECRET is not set in .env");
            }
        } catch (Exception e) {
            System.err.println("Dotenv failed to load .env, falling back to System properties: " + e.getMessage());
            SECRET_KEY = System.getProperty("JWT_SECRET");
            if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
                throw new IllegalStateException("JWT_SECRET is not set.");
            }
        }

        key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public static String generateAccessToken(AuthUserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", userDetails.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String generateRefreshToken(AuthUserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static boolean validateToken(String token, AuthUserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private static boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public static AccountType extractUserRole(String token) {
        return AccountType.valueOf(extractClaim(token, claims -> claims.get("role", String.class)));
    }

    private static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
}
