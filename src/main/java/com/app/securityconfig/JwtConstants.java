package com.app.securityconfig;

public class JwtConstants {
	public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60;
	public static final String SIGNING_KEY = "karna";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String AUTHORITIES_KEY = "scopes";
}
