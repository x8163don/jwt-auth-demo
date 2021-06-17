package com.demo.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demo.dao.IAccountDao;
import com.demo.entity.Account;
import com.demo.model.BasicUserDetail;

/**
 * @author Bevis
 * @date 2019/08/15
 * @description 自訂取得帳戶資訊用 Spring Security 中主要使用 loadUserByUsername 取得驗證階段所需帳戶資訊
 */
@Service
public class BasicUserDetailService implements UserDetailsService {

	/**
	 * 對 SimpleGrantedAuthority 而言若加上此前綴代表"角色"，可用hasRole()驗證
	 * 無加上前綴者則為"權限"，可用hasAuthority()驗證
	 */
	private static final String ROLE_PERFIX = "ROLE_";

	@Autowired
	IAccountDao accountDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// 查詢帳戶
		Optional<Account> account = accountDao.findById(username);

		// 驗證帳戶是否存在
		if (!account.isPresent()) {
			throw new UsernameNotFoundException(username);
		}

		// 驗證使用者是否存在
		if (account.get().getUser() == null) {
			throw new UsernameNotFoundException(username);
		}

		// 建立角色及權限清單
		Set<SimpleGrantedAuthority> authoritys = new HashSet<>();

		// 新增使用者角色
		authoritys.addAll(account.get().getUser().getRoles().stream()
				.map(x -> new SimpleGrantedAuthority(ROLE_PERFIX + x.getRoleId())).collect(Collectors.toSet()));

		// 新增使用者權限
		authoritys.addAll(account.get().getUser().getPermissions().stream()
				.map(x -> new SimpleGrantedAuthority(x.getPermissionId())).collect(Collectors.toSet()));

		// 回傳
		return new BasicUserDetail(account.get().getAccountId(), account.get().getPassowrd(), authoritys);
	}

}
