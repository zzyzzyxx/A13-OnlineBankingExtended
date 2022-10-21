package com.coderscampus.assignment13.web;

import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.AccountService;
import com.coderscampus.assignment13.service.AddressService;
import com.coderscampus.assignment13.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private AccountService accountService;

	@GetMapping("/register")
	public String getCreateUser(ModelMap model) {
		model.put("user", new User());

		return "register";
	}

	@PostMapping("/register")
	public String postCreateUser(User user) {
		System.out.println(user);
		userService.saveUser(user);
		return "redirect:/register";
	}

	@GetMapping("/users")
	public String getAllUsers(ModelMap model) {
		Set<User> users = userService.findAll();
		model.put("users", users);
		if (users.size() == 1) {
			model.put("user", users.iterator().next());
			if (users.iterator().next().getAddress() == null) {
				model.put("address", new Address());
			} else {
				Address userAddress = addressService.findUserAddressById(users.iterator().next().getUserId());
				model.put("address", userAddress);
			}
		}
		return "users";
	}

	@PostMapping("/users")
	public String postUpdateUser() {
		return "users";
	}

	@GetMapping("/users/{userId}")
	public String getOneUser(ModelMap model, @PathVariable Long userId) {
		User user = userService.findById(userId);
		model.put("users", Arrays.asList(user));
		model.put("user", user);
		if (user.getAddress() == null) {
			model.put("address", new Address());
		} else {
			Address userAddress = addressService.findUserAddressById(user.getUserId());
			model.put("address", userAddress);
		}
		if (user.getAccounts() != null) {
			model.put("accounts", user.getAccounts());
		}
		return "users";
	}

	@PostMapping("/users/{userId}")
	public String postOneUser(@PathVariable Long userId, User user) {
		user.getAddress().setUserId(userId);
		user.getAddress().setUser(user);
		
		if(user.getAddress().getUserId() == null) {
			user.getAddress().setUserId(userId);
		}
		userService.saveUser(user);
		return "redirect:/users/" + user.getUserId();
	}

	@PostMapping("/users/{userId}/delete")
	public String deleteOneUser(@PathVariable Long userId) {
		userService.delete(userId);
		return "redirect:/users";
	}

	@GetMapping("/users/{userId}/accounts/{accountId}")
	public String displayUserAccount(ModelMap model, @PathVariable Long userId, @PathVariable Long accountId) {
		User user = userService.findById(userId);
		Account userAccount = accountService.fetchUserAccountById(accountId);
		model.put("user", user);
		model.put("account", userAccount);
		return "account";
	}

	@PostMapping("/users/{userId}/accounts/{accountId}")
	public String saveAccountDetails (@PathVariable Long userId, @PathVariable Long accountId, Account account) {
		User user = userService.findById(userId);
		Account userAccount = accountService.fetchUserAccountById(accountId);
		userAccount.setAccountName(account.getAccountName());
		accountService.save(account);
		userService.saveUser(user);
		return "redirect:/users/{userId}/accounts/{accountId}";
	}
	
	@PostMapping("/users/{userId}/accounts")
	public String createUserAccount (@PathVariable Long userId) {
		User user = userService.findById(userId);
		Account account = new Account();
		account.getUsers().add(user);
		user.getAccounts().add(account);
		account.setAccountName("Account #" + user.getAccounts().size());
		accountService.save(account);
		userService.saveUser(user);
		return "redirect:/users/{userId}";
		
	}

}
