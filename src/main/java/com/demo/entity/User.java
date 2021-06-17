package com.demo.entity;

import java.util.Calendar;
import java.util.Collections;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "CUR_USER")
public class User {

	@Id
	@GeneratedValue
	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "EMAIL")
	private String email;

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

	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@JoinTable(//
			name = "CUR_USER_ROLE", //
			joinColumns = { @JoinColumn(name = "USER_ID") }, //
			inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") }//
	)
	Set<Role> roles = Collections.emptySet();

	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@JoinTable(//
			name = "CUR_USER_PERMISSION", //
			joinColumns = { @JoinColumn(name = "USER_ID") }, //
			inverseJoinColumns = { @JoinColumn(name = "PERMISSION_ID") }//
	)
	Set<Permission> permissions = Collections.emptySet();
}
