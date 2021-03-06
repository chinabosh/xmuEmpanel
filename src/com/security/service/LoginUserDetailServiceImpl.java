package com.security.service;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.security.pojo.LoginUserDetails;
import com.pojo.LoginUser;
import com.service.UserService;

/**
 * @Type: com.cadre.security.service.LoginUserDetailServiceImpl.java
 * @ClassName: LoginUserDetailServiceImpl
 * @Description: 用于spring security 用户登录<br/>
 * 
 */
@Component("loginUserDetailServiceImpl")
public class LoginUserDetailServiceImpl implements UserDetailsService {

	@Autowired
	UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 用户登陆
		LoginUser user = userLogin(username);		//数据库的user实例
		if (user != null) {
			LoginUserDetails userDetails = new LoginUserDetails(username, user.getPassword(), generateSimpleAuth(user.getRoleId()));
			userDetails.setUser(user);
			return userDetails;
		}

		// 如果程序运行到此，说明登录失败了，只能返回null;
		throw new UsernameNotFoundException("用户不存在");
	}

	/**
	 * 用户登陆
	 * 
	 * @param username
	 * @return
	 */
	private LoginUser userLogin(String username) {
		if (StringUtils.isBlank(username)) {
			return null;
		}
		LoginUser loginUser = userService.loginUserFindByUsername(username);
		return loginUser;

	}
	
	
	/**
	 * 这里赋予用户角色，和xml配置相互配合
	 * 产生简单的spring security验证需要的角色。 可以传递一个user的属性作为该方法的参数，然后根据参数值不同赋予不同角色。
	 * 
	 * @return
	 */
	private Collection<SimpleGrantedAuthority> generateSimpleAuth(int roleId) {
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authority;
		switch (roleId) {
		case 1:
			authority = new SimpleGrantedAuthority("ROLE_ADMIN");
			break;
		case 2:
			authority = new SimpleGrantedAuthority("ROLE_ORG");
			break;
		case 3:
			authority = new SimpleGrantedAuthority("ROLE_PSG");
			break;
		case 4:
			authority = new SimpleGrantedAuthority("ROLE_TSG");
			break;
		case 5:
			authority = new SimpleGrantedAuthority("ROLE_STAFF");
			break;
		default:
			authority = new SimpleGrantedAuthority("ROLE_STAFF");
			break;
		}
		
		authorities.add(authority);
		return authorities;
	}
}
