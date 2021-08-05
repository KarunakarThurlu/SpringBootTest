package com.app.securityconfig;

import static com.app.securityconfig.JwtConstants.TOKEN_PREFIX;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.app.serviceimpl.CustomUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String header = request.getHeader("Authorization");
		String username = null;
		String authToken = null;
		if (header != null) {
			
			//From PostMan Or any WebApps
			if(header.startsWith(TOKEN_PREFIX)) {
				authToken = header.replace(TOKEN_PREFIX,"");
				try {
					username = jwtUtil.getUsernameFromToken(authToken);
				} catch (IllegalArgumentException e) {
					logger.error("an error occured during getting username from token", e);
				} catch (ExpiredJwtException e) {
					logger.warn("the token is expired and not valid anymore", e);
				}
			}else {
				//From SwaggerUI
				authToken=header;
				try {
					username = jwtUtil.getUsernameFromToken(header);
				} catch (IllegalArgumentException e) {
					logger.error("an error occured during getting username from token", e);
				} catch (ExpiredJwtException e) {
					logger.warn("the token is expired and not valid anymore", e);
				} catch(SignatureException e) {
					throw new RuntimeException("Invalid Token");
				}
			}
		} 
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

			if (jwtUtil.validateToken(authToken, userDetails)) {
				UsernamePasswordAuthenticationToken authentication = jwtUtil.getAuthentication(authToken, SecurityContextHolder.getContext().getAuthentication(), userDetails);
				//UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				logger.info("authenticated user " + username + ", setting security context");
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		filterChain.doFilter(request, response);

	}

}
