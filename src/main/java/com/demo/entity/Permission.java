package com.demo.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "CUR_PERMISSION")
public class Permission {

	@Id
	@GeneratedValue
	@Column(name = "PERMISSION_ID")
	private String permissionId;

	@Column(name = "PERMISSION_NAME")
	private String permissionName;

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
}
