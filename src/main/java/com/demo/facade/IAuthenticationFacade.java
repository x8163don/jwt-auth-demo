package com.demo.facade;

import com.demo.bean.SignInBean;
import com.demo.model.TokensModel;

/**
 * @author Bevis
 * @date 2019/08/15
 * @description
 */
public interface IAuthenticationFacade {
	
	/**
	 * @author Bevis
	 * @date 2019/08/15
	 * @description 登入
	 * @param request
	 */
	TokensModel login(SignInBean request);

	/**
	 * @author Bevis
	 * @date 2019/08/15
	 * @description 刷新Token
	 * @param oldToken
	 * @return
	 */
	TokensModel refresh(TokensModel tokens);
	
}
