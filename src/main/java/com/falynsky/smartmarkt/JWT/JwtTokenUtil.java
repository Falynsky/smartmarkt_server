package com.falynsky.smartmarkt.JWT;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long ONE_HOUR_IN_SECONDS = 60 * 60;
    public static final long NUMBER_OF_HOURS = 5;
    public static final long JWT_TOKEN_VALIDITY = NUMBER_OF_HOURS * ONE_HOUR_IN_SECONDS;
    public static final long JWT_TOKEN_VALIDITY_IN_MILLIS = JWT_TOKEN_VALIDITY * 1000;

    @Value("${jwt.secret}")
    private String secret;

    public String getUsernameFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims.getSubject();
    }

    private Claims getAllClaimsFromToken(String token) {
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        JwtParser jwtParser = Jwts.parser().setSigningKey(secretBytes);
        Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        return claimsJws.getBody();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String subject = userDetails.getUsername();
        long currentTimeMillis = System.currentTimeMillis();
        Date now = new Date(currentTimeMillis);
        Date expirationTime = new Date(currentTimeMillis + JWT_TOKEN_VALIDITY_IN_MILLIS);
        String encodedSecret = Base64.getEncoder().encodeToString(secret.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS512, encodedSecret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        boolean areUsernamesEqual = areUsernamesEqual(token, userDetails);
        boolean isTokenExpired = isTokenExpired(token);
        return areUsernamesEqual && !isTokenExpired;
    }

    private boolean areUsernamesEqual(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        if (userDetails == null) {
            return false;
        }
        String userDetailsUsername = userDetails.getUsername();
        return username.equals(userDetailsUsername);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationFromToken(token);
        Date today = new Date();
        return expiration.before(today);
    }

    private Date getExpirationFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims.getExpiration();
    }
}