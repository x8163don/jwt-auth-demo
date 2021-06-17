package com.demo.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import com.demo.model.BasicUserDetail;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@SpringBootTest
@RunWith(PowerMockRunner.class)
@PrepareForTest({ JwtTokenUtil.class })
@PowerMockIgnore("javax.crypto.*")
public class JwtTokenUtilTest {

	@InjectMocks
	JwtTokenUtil jwtTokenUtil;

	private static final String SECRET = "995e4ec8-869a-407c-a07f-43968b90e110";
	private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
	private static final Long DEFAULT_EXPIRATION = 3600L;
	private static final Long DEFAULT_REFRESH_EXPIRATION = 86400L;

	@Before
	public void setJwtTokenUtilField() {
		// 設定私鑰及到期時間
		ReflectionTestUtils.setField(jwtTokenUtil, "secret", SECRET, String.class);
		ReflectionTestUtils.setField(jwtTokenUtil, "expiration", DEFAULT_EXPIRATION, Long.class);
		ReflectionTestUtils.setField(jwtTokenUtil, "refreshExpiration", DEFAULT_REFRESH_EXPIRATION, Long.class);
	}

	/**
	 * <pre>
	 * 	測試對象 : generateToken 
	 * 	測試情境 : 給予正常使用者資訊建立Access Token
	 * 	預期結果 : 正常產生可驗證的Token
	 * </pre>
	 */
	@Test
	public void when_PutNormalUserDetailToGenerateToken_Expect_Success() throws Exception {

		// Arrange
		BasicUserDetail normalUser = BasicUserDetail.builder() //
				.username("Account")//
				.password("Password")//
				.build();

		// Act
		String token = jwtTokenUtil.generateToken(normalUser);
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

		// Assert
		assertEquals("Account", claims.getSubject());
		assertEquals(JwtTokenUtil.SCOPE_ACCESS, claims.get(JwtTokenUtil.SCOPE));
		assertNotNull(claims.getExpiration());
		assertNotNull(claims.getIssuedAt());
	}

	/**
	 * <pre>
	 * 	測試對象 : generateToken 
	 * 	測試情境 : 給予無Username資訊建立Access Token
	 * 	預期結果 : 拋出UsernameNotFoundException
	 * </pre>
	 */
	@Test(expected = UsernameNotFoundException.class)
	public void when_PutNoUsernameUserDetailToGenerateToken_Expect_Exception() throws Exception {

		// Arrange
		BasicUserDetail noUsernameUser = BasicUserDetail.builder() //
				.password("Password") //
				.build();

		// Act
		jwtTokenUtil.generateToken(noUsernameUser);
	}

	/**
	 * <pre>
	 * 	測試對象 : generateRefreshToken 
	 * 	測試情境 : 給予正常使用者資訊建立Refresh Token
	 * 	預期結果 : 正常產生可用的Refresh Token
	 * </pre>
	 */
	@Test
	public void when_PutNormalUserDetailToGenerateRefreshToken_Expect_Success() throws Exception {

		// Arrange
		BasicUserDetail normalUser = BasicUserDetail.builder() //
				.username("Account")//
				.password("Password")//
				.build();

		// Act
		String token = jwtTokenUtil.generateToken(normalUser);
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

		// Assert
		assertEquals("Account", claims.getSubject());
		assertEquals(JwtTokenUtil.SCOPE_ACCESS, claims.get(JwtTokenUtil.SCOPE));
		assertNotNull(claims.getExpiration());
		assertNotNull(claims.getIssuedAt());
	}

	/**
	 * <pre>
	 * 	測試對象 : generateRefreshToken 
	 * 	測試情境 : 給予無Username資訊建立Refresh Token
	 * 	預期結果 : 拋出UsernameNotFoundException
	 * </pre>
	 */
	@Test(expected = UsernameNotFoundException.class)
	public void when_PutNoUsernameUserDetailToGenerateRefreshToken_Expect_Exception() throws Exception {

		// Arrange
		BasicUserDetail noUsernameUser = BasicUserDetail.builder() //
				.password("Password") //
				.build();

		// Act
		jwtTokenUtil.generateToken(noUsernameUser);
	}

	/**
	 * <pre>
	 * 	測試對象 : getClaims 
	 * 	測試情境 : 給予格式正確且未到期Token取得Claims
	 * 	預期結果 : 可取得Token內的所有Claims
	 * </pre>
	 */
	@Test
	public void when_PutNormalTokenToGetClaims_Expect_Success() throws Exception {

		// Arrange
		BasicUserDetail normalUser = BasicUserDetail.builder() //
				.username("Account")//
				.password("Password")//
				.build();

		String token = jwtTokenUtil.generateToken(normalUser);

		// Act
		Claims claims = jwtTokenUtil.getClaims(token);

		// Assert
		assertNotNull(claims);
		assertEquals(4, claims.size());
	}

	/**
	 * <pre>
	 * 	測試對象 : getSubject 
	 * 	測試情境 : 給予格式正確且未到期Token取得Subject
	 * 	預期結果 : 可取得Token內的Subject
	 * </pre>
	 */
	@Test
	public void when_PutNormalTokenToGetSubject_Expect_Success() throws Exception {

		// Arrange
		BasicUserDetail normalUser = BasicUserDetail.builder() //
				.username("Account")//
				.password("Password")//
				.build();

		String token = jwtTokenUtil.generateToken(normalUser);

		// Act
		String subject = jwtTokenUtil.getSubject(token);

		// Assert
		assertEquals("Account", subject);
	}

	/**
	 * <pre>
	 * 	測試對象 : getExpiration 
	 * 	測試情境 : 給予格式正確且未到期Token取得到期時間
	 * 	預期結果 : 可取得Token內的到期時間
	 * </pre>
	 */
	@Test
	public void when_PutNormalTokenToGetExpiration_Expect_Success() throws Exception {

		// Arrange
		BasicUserDetail normalUser = BasicUserDetail.builder() //
				.username("Account")//
				.password("Password")//
				.build();

		// 預設回傳
		Date expirationDate = new Date(System.currentTimeMillis() + DEFAULT_EXPIRATION * 1000);
		PowerMockito.whenNew(Date.class).withAnyArguments().thenReturn(expirationDate);

		// 建立Token
		String token = jwtTokenUtil.generateToken(normalUser);

		// Act
		Date expiration = jwtTokenUtil.getExpiration(token);

		// Assert
		// jjwt的Tool 會將毫秒去除因此驗證處須先 /1000 再 *1000 將毫秒去除
		assertEquals(expirationDate.getTime() / 1000 * 1000, expiration.getTime());
	}

	/**
	 * <pre>
	 * 	測試對象 : isValidate 
	 * 	測試情境 : 給予格式正確且未到期Token進行驗證
	 * 	預期結果 : 應回傳True
	 * </pre>
	 */
	@Test
	public void when_PutNormalTokenToIsValidate_Expect_True() throws Exception {

		// Arrange
		BasicUserDetail normalUser = BasicUserDetail.builder() //
				.username("Account")//
				.password("Password")//
				.build();

		// 建立Token
		String token = jwtTokenUtil.generateToken(normalUser);

		// Act
		boolean isValidate = jwtTokenUtil.isValidate(token);

		// Assert
		assertTrue(isValidate);
	}

	/**
	 * <pre>
	 * 	測試對象 : isValidate 
	 * 	測試情境 : 給予格式錯誤Token進行驗證
	 * 	預期結果 : 應回傳False
	 * </pre>
	 */
	@Test
	public void when_PutFakeTokenToIsValidate_Expect_False() throws Exception {

		// Arrange
		// 建立Token
		String fakeToken = UUID.randomUUID().toString();

		// Act
		boolean isValidate = jwtTokenUtil.isValidate(fakeToken);

		// Assert
		assertFalse(isValidate);
	}

	/**
	 * <pre>
	 * 	測試對象 : isValidate 
	 * 	測試情境 : 給予格已到期Token進行驗證
	 * 	預期結果 : 應回傳False
	 * </pre>
	 */
	@Test
	public void when_PutExpirationTokenToIsValidate_Expect_False() throws Exception {

		// Arrange
		BasicUserDetail normalUser = BasicUserDetail.builder() //
				.username("Account")//
				.password("Password")//
				.build();

		// 預設回傳
		Date expirationDate = new Date(System.currentTimeMillis() - 1000);
		PowerMockito.whenNew(Date.class).withAnyArguments().thenReturn(expirationDate);

		// 建立已到期Token
		String token = jwtTokenUtil.generateToken(normalUser);

		// Act
		boolean isValidate = jwtTokenUtil.isValidate(token);

		// Assert
		assertFalse(isValidate);
	}

}
