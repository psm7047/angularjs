package com.mycompany.webapp.security;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
	// 시큐리티 키, 비밀 키 (유출되면 안됨)
	private static final String secretKey = "sercret";
	
	// JWT 생성 : 사용자 아이디 포함 
	public static String createToken(String uid) {
		String token = null;
		try {
			/*
			 * token = Jwts.builder() .setHeaderParam("typ", "JWT") //토큰 종류
			 * .setHeaderParam("alg", "HS256") //암호화 알고리즘 종류 .setExpiration(new Date(new
			 * Date().getTime() + 1000*60*60*12)) //토큰의 유효기간 .claim("uid",uid) //토큰에 저장되는
			 * 데이터 .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8")) //비밀키
			 * .compact(); //모든 내용을 묶기
			 */			
			JwtBuilder builder = Jwts.builder();
			builder.setHeaderParam("typ", "JWT");	//토큰 종류
			builder.setHeaderParam("alg", "HS256");	//암호화 알고리즘 종류
			builder.setExpiration(new Date(new Date().getTime() + 1000*60*60*12));	//토큰의 유효기간
			builder.claim("uid",uid);	//토큰에 저장되는 데이터
			builder.signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"));	//비밀키
			token = builder.compact();	//모든 내용을 묶기
		}catch(Exception e) {
			e.printStackTrace();
		}
		return token;
		
	}
	
	//JWT를 파싱해서 uid 얻기
	public static String getUid(String token) {
		String uid = null;
		try {
			JwtParser parser = Jwts.parser();
			parser.setSigningKey(secretKey.getBytes("UTF-8"));
			Jws<Claims> jws = parser.parseClaimsJws(token);
			Claims claims =jws.getBody();
			uid = claims.get("uid", String.class);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return uid;
	}
	
	//JWT 유효성 검사 : 유효기간 확인
	public static boolean validateToken(String token) {
		boolean validate = false;
		
		try {
			JwtParser parser = Jwts.parser();
			parser.setSigningKey(secretKey.getBytes("UTF-8"));
			Jws<Claims> jws = parser.parseClaimsJws(token);
			Claims claims =jws.getBody();
			validate = claims.getExpiration().after(new Date());
			/*if(validate) {
				long remainMillSecs = claims.getExpiration().getTime() - new Date().getTime();
				logger.info("유효기간이 " + remainMillSecs/1000 + "초 남았습니다.");
			}*/
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return validate;
	}
	
	//테스트
//	public static void main(String[] args) {
//		//토큰 생성
//		String jwt = createToken("user1");
//		logger.info(jwt);
//		
//		//지연
//		try { Thread.sleep(5000); }catch(Exception e) {}
//		
//		if(validateToken(jwt)) {
//		//토큰 정보 얻기
//		String uid = getUid(jwt);
//		logger.info(uid);
//		}
//	}
	
	
}