package com.demo.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * @author Bevis
 * @date 2019/08/14 17:58:17
 * @description JWT Token 用Util
 */
@Component
public class JwtTokenUtil {

	public static final String SCOPE = "scope";
	public static final String SCOPE_ACCESS = "access";
	public static final String SCOPE_REFRESH = "refrsh";

	@Autowired
	HttpServletRequest request;

	@Value("${system.security.secret}")
	private String secret;

	@Value("${system.security.expiration}")
	private Long expiration;

	@Value("${system.security.refresh-expiration}")
	private Long refreshExpiration;

	/**
	 * @author Bevis
	 * @date 2019/08/14
	 * @description 產生 JWT Token
	 * @param userDetails
	 * @param claims
	 */
	public String generateToken(UserDetails userDetails) {
		// 驗證
		if (StringUtils.isEmpty(userDetails.getUsername())) {
			throw new UsernameNotFoundException("缺少帳戶資訊");
		}

		// 回傳Access Token
		return Jwts.builder() //
				.setSubject(userDetails.getUsername()) //
				.claim(SCOPE, SCOPE_ACCESS) //
				.setIssuedAt(new Date()) //
				.setExpiration(new Date(System.currentTimeMillis() + expiration * 1000)) //
				.signWith(getSecretKey()) //
				.compact();
	}

	/**
	 * @author Bevis
	 * @date 2019/08/16
	 * @description 產生Refresh Token
	 */
	public String generateRefreshToken(UserDetails userDetails) {
		// 驗證
		if (StringUtils.isEmpty(userDetails.getUsername())) {
			throw new UsernameNotFoundException("缺少帳戶資訊");
		}

		return Jwts.builder() //
				.setSubject(userDetails.getUsername()) //
				.setId(UUID.randomUUID().toString()).setIssuedAt(new Date()) //
				.claim(SCOPE, SCOPE_REFRESH) //
				.setIssuedAt(new Date()) //
				.setExpiration(new Date(System.currentTimeMillis() + refreshExpiration * 1000)) //
				.signWith(getSecretKey()) //
				.compact();
	}

	/**
	 * @author Bevis
	 * @date 2019/08/14
	 * @description 取得JWT Token Payload 所有內容
	 */
	public Claims getClaims() {
		return this.getClaims(getAccessToken());
	}

	/**
	 * @author Bevis
	 * @date 2019/08/14
	 * @description 取得JWT Token Payload 所有內容
	 */
	public Claims getClaims(String token) {
		return Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token).getBody();
	}

	/**
	 * @author Bevis
	 * @date 2019/08/14
	 * @description 取得JWT Token 中的主體(通常是User Account)
	 */
	public String getSubject() {
		return getSubject(getAccessToken());
	}

	/**
	 * @author Bevis
	 * @date 2019/08/14
	 * @description 取得JWT Token 中的主體(通常是User Account)
	 */
	public String getSubject(String token) {
		return Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token).getBody().getSubject();
	}

	/**
	 * @author Bevis
	 * @date 2019/08/14
	 * @description 取得JWT Token 中的到期時間
	 */
	public Date getExpiration() {
		return getExpiration(getAccessToken());
	}

	/**
	 * @author Bevis
	 * @date 2019/08/14
	 * @description 取得JWT Token 中的到期時間
	 */
	public Date getExpiration(String token) {
		return Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token).getBody().getExpiration();
	}

	/**
	 * @author Bevis
	 * @date 2019/08/14
	 * @description 驗證JWT Token 是否正確，若Token已到期結果同為未通過
	 * @return
	 */
	public boolean isValidate() {
		return isValidate(getAccessToken());
	}

	/**
	 * @author Bevis
	 * @date 2019/08/14
	 * @description 驗證JWT Token 是否正確，若Token已到期結果同為未通過
	 * @return
	 */
	public boolean isValidate(String token) {
		try {
			Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

	/**
	 * @author Bevis
	 * @date 2019/08/14
	 * @description 驗證 Token 是否為 AccessToken
	 * @return
	 */
	public boolean isAccessToken() {
		return Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(getAccessToken()).getBody().get(SCOPE)
				.equals(SCOPE_ACCESS);
	}

	/**
	 * @author Bevis
	 * @date 2019/08/14
	 * @description 驗證 Token 是否為 RefreshToken
	 * @return
	 */
	public boolean isRefreshToken(String refreshToken) {
		return Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(refreshToken).getBody().get(SCOPE)
				.equals(SCOPE_REFRESH);
	}

	// 取得存取用Token
	private String getAccessToken() {
		return request.getHeader(HttpHeaders.AUTHORIZATION);
	}

	// 取得JWT加密用Key
	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}
}
