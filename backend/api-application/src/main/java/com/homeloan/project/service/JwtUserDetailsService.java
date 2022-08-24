package com.homeloan.project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.homeloan.project.model.AuthUser;
import com.homeloan.project.model.Customer;
import com.homeloan.project.model.SavingsAccount;
import com.homeloan.project.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	CustomerRepository customerRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Customer> customer = customerRepository.findById(username);
		if (customer.isPresent()) {
			return buildUserForAuthentication(customer.get(),new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("Customer not found with username: " + username);
		}
	}

	//Fill your extended Customer object (CurrentUser) here and return it
	private User buildUserForAuthentication(Customer customer,
											List<GrantedAuthority> authorities) {
		String username = customer.getUserId();
		String password = customer.getPassword();
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;

		AuthUser authUser = new AuthUser(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		authUser.setUserId(username);
		authUser.setSavingsAccount(customer.getSavingsAccount());
		return authUser;
	}

}