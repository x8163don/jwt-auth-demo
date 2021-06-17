package com.demo.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.demo.bean.SignInBean;
import com.demo.facade.IAuthenticationFacade;
import com.demo.model.BasicUserDetail;
import com.demo.model.TokensModel;
import com.demo.util.JwtTokenUtil;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserDetailsService basicUserDetailService;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Override
	public TokensModel login(SignInBean request) {

		// 取得UserDeatil
		BasicUserDetail userDeatil = (BasicUserDetail) basicUserDetailService.loadUserByUsername(request.getUsername());

		// 驗證帳密
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		// 寫入SecurityContext
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// 回傳Token
		TokensModel reuslt = TokensModel //
				.builder() //
				.accessToken(jwtTokenUtil.generateToken(userDeatil)) //
				.refreshToken(jwtTokenUtil.generateRefreshToken(userDeatil)) //
				.build();

		return reuslt;
	}

	@Override
	public TokensModel refresh(TokensModel tokens) {

		// 驗證Token
		if (!jwtTokenUtil.isValidate(tokens.getRefreshToken())) {
			throw new BadCredentialsException("Token 驗證失敗");
		}

		// 驗證是否為Refresh Token
		if (!jwtTokenUtil.isRefreshToken(tokens.getRefreshToken())) {
			throw new BadCredentialsException("非Refresh Token");
		}

		// 取得使用者資訊
		BasicUserDetail userDeatil = (BasicUserDetail) basicUserDetailService
				.loadUserByUsername(jwtTokenUtil.getSubject(tokens.getRefreshToken()));

		// 更換Access Token
		tokens.setAccessToken(jwtTokenUtil.generateToken(userDeatil));

		return tokens;
	}

}
