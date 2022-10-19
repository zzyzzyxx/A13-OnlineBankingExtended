package com.coderscampus.assignment13.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;

@Service
public class AccountService {
	@Autowired
	AccountRepository accountRepo;

	public Account saveAccountToUser(Account account, User user) {
		account.getUsers().add(user);
		user.getAccounts().add(account);
		return accountRepo.save(account);
	}

	public Account fetchUserAccountById(Long accountId) {
		return accountRepo.getOne(accountId);
	}

	public Account save(Account account) {
		return accountRepo.save(account);
	}
}
