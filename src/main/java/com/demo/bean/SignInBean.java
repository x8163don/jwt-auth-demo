package com.demo.bean;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Bevis
 * @date 2019/08/15
 * @description 登入用請求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInBean implements Serializable {

	private static final long serialVersionUID = -2192708723351338867L;

	@NotEmpty
//	@Pattern(regexp = "")
	private String username;

	@NotEmpty
//	@Pattern(regexp = "")
	private String password;

}