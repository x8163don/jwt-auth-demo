package com.demo.bean;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokensBean implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;

	@JsonProperty("access_token")
	private String accessToken;

	@NotEmpty(groups = Refresh.class)
	@JsonProperty("refresh_token")
	private String refreshToken;

	public interface Refresh {
	}
}
