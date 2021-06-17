package com.demo.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "CUR_ACCOUNT")
public class Account {
	
	@Id
	@GeneratedValue
	@Column(name = "ACCOUNT_ID")
	private String accountId;
	
	@Column(name = "PASSWORD")
	private String passowrd;
	
	@Column(name = "IS_ENABLE")
	private Boolean isEnable;

	@Column(name = "CREATE_BY")
	private String createBy;

	@Column(name = "CREATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar createDate;

	@Column(name = "UPDATE_BY")
	private String updateBy;

	@Column(name = "UPDATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar updateDate;

	@OneToOne
	@JoinColumn(name = "USER_ID")
	User user;

}
