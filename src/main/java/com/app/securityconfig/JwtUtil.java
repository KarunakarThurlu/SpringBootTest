package com.app.securityconfig;

import static com.app.securityconfig.JwtConstants.ACCESS_TOKEN_VALIDITY_SECONDS;
import static com.app.securityconfig.JwtConstants.AUTHORITIES_KEY;
import static com.app.securityconfig.JwtConstants.SIGNING_KEY;
import static com.app.securityconfig.JwtConstants.SECRET_KEY;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
	
	public String generateToken(Authentication authentication) {
		final String authorities = authentication
				.getAuthorities()
				.stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		return Jwts.builder()
				.setSubject(authentication.getName())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
				.claim(AUTHORITIES_KEY, authorities).signWith(key(), SignatureAlgorithm.HS256).setId(SECRET_KEY)
				.compact();
	}

	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SIGNING_KEY));
	}

	public String getUsernameFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
			return true;
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

}
