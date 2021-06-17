package com.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.entity.Account;

@Repository
public interface IAccountDao extends JpaRepository<Account, String> {

}
