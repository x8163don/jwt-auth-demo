package com.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.demo.aop.JwtAuthenticationTokenFilter;
import com.demo.handler.JwtAuthenticationEntryPoint;

/**
 * @author Bevis
 * @date 2019/08/15
 * @description Spring Secruity 設定檔
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService basicUserDetailService;

	/**
	 * @author Bevis
	 * @date 2019/08/15
	 * @description 註冊檢驗 Request 是否帶有 Token 用 Filter
	 */
	@Bean
	public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
		return new JwtAuthenticationTokenFilter();
	}

	/**
	 * @author Bevis
	 * @date 2019/08/15
	 * @description 註冊密碼加密器
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * @author Bevis
	 * @date 2019/08/15
	 * @description Authentication管理
	 */
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	/**
	 * @author Bevis
	 * @date 2019/08/15
	 * @description 註冊DaoAuthentication認證提供者
	 */
	@Bean
	public AuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(basicUserDetailService);
		return daoAuthenticationProvider;
	}

	/**
	 * @author Bevis
	 * @date 2019/08/15
	 * @description 註冊DaoAuthentication認證提供者
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider()) //
				.userDetailsService(basicUserDetailService) //
				.passwordEncoder(passwordEncoder());
	}

	/**
	 * @author Bevis
	 * @date 2019/08/15
	 * @description 設定攔截條件
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http //
				// 設定EexceptionHandling
				.exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint()) //
				.and() //
				// 關閉FrameOptions
				.headers().frameOptions().disable() //
				.and()//
				// 關閉 CSRF
				.csrf().disable() //
				// 設定為無狀態
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //
				.and() //
				// 設定需Request規則
				.authorizeRequests() //
				// 開放登入及刷新無須進行身分驗證
				.antMatchers("/auth/**").permitAll() //
				// 開放H2無須進行身分驗證
				.antMatchers("/h2/**").permitAll() //
				// 開放靜態資源(限用GET)無須進行身分驗證
				.antMatchers(HttpMethod.GET, "/", "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js").permitAll() //
				// 其餘Request皆須進行身分驗證
				.anyRequest().authenticated() //
				.and() //
				// 關閉快取
				.headers().cacheControl();

		// 註冊檢驗 Request 用 Filter
		http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

	}

}
