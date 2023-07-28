package com.app.securityconfig;

public class JwtConstants {
	
	private JwtConstants() {
		super();
	}
	
	public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60l;
	public static final String SIGNING_KEY = "SpRinGB00tapPQSpRinGB0tapPJSp0tapPQSpRinGB0tRin0tapP";
	public static final String SECRET_KEY = "!SpR!nGBO0t@1*90";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String AUTHORITIES_KEY = "scopes";
}
