package com.demo.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.bean.SignInBean;
import com.demo.bean.TokensBean;
import com.demo.bean.TokensBean.Refresh;
import com.demo.facade.IAuthenticationFacade;
import com.demo.model.TokensModel;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired
	IAuthenticationFacade authenticationFacade;

	@RequestMapping(method = RequestMethod.POST, path = "/login")
	public ResponseEntity<TokensBean> login(@RequestBody @Validated SignInBean request,
			HttpServletResponse httpServletResponse) {

		// 建立回傳
		TokensBean result = new TokensBean();

		// 登入
		TokensModel token = authenticationFacade.login(request);

		// 設定JWT Token至Header
		httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, token.getAccessToken());

		// 回傳
		BeanUtils.copyProperties(token, result);
		return ResponseEntity.ok(result);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/refresh")
	public ResponseEntity<TokensBean> refresh(@RequestBody @Validated({ Refresh.class }) TokensBean tokens,
			HttpServletResponse httpServletResponse) {

		// 建立回傳
		TokensBean result = new TokensBean();

		// 刷新Token
		TokensModel resultToken = authenticationFacade
				.refresh(TokensModel.builder().refreshToken(tokens.getRefreshToken()).build());

		// 回傳
		BeanUtils.copyProperties(resultToken, result);
		return ResponseEntity.ok(result);
	}

}
