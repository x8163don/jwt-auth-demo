package com.demo.aop;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import com.demo.util.JwtTokenUtil;

/**
 * @author Bevis
 * @date 2019/08/14 16:42:24
 * @description 此 Filter 主要用於檢查 Request 是否有提供 Token ，並檢查 Token 資訊是否已寫入
 *              SecurityContextHolder
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	@Autowired
	UserDetailsService basicUserDetailService;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 取得Token
		String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

		// 若無Token則跳過
		if (StringUtils.isEmpty(accessToken) || SecurityContextHolder.getContext().getAuthentication() != null) {
			filterChain.doFilter(request, response);
			return;
		}

		// 驗證Token格式，若Token正確將User訊息寫入SecurityContextHolder
		if (jwtTokenUtil.isValidate() && jwtTokenUtil.isAccessToken()) {
			// 取得User資訊
			UserDetails userDetail = this.basicUserDetailService.loadUserByUsername(jwtTokenUtil.getSubject());

			// 權限寫入SecurityContextHolder
			UsernamePasswordAuthenticationToken securityToken = new UsernamePasswordAuthenticationToken(
					userDetail.getUsername(), userDetail.getPassword(), userDetail.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(securityToken);
		}

		filterChain.doFilter(request, response);
	}

}
