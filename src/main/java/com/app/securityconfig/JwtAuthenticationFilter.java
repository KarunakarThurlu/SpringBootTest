package com.app.securityconfig;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.app.serviceimpl.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.app.securityconfig.JwtConstants.TOKEN_PREFIX;;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		 // look for Bearer auth header
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        	filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.replace(TOKEN_PREFIX,"");
        final boolean isValid = jwtUtil.validateJwtToken(token);
        if (!isValid) {
            // validation failed or token expired
        	filterChain.doFilter(request, response);
            return;
        }

        // set user details on spring security context
        final String userName = jwtUtil.getUsernameFromToken(token);
        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // continue with authenticated user
        filterChain.doFilter(request, response);
	}

	/*
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException {

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

			if (jwtUtil.validateJwtToken(authToken, userDetails)) {
				UsernamePasswordAuthenticationToken authentication = jwtUtil.getAuthentication(authToken, SecurityContextHolder.getContext().getAuthentication(), userDetails);
				//UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				logger.info("authenticated user " + username + ", setting security context");
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		filterChain.doFilter(request, response);

	} */

}
