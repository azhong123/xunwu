package com.spring.security;

import com.spring.entity.User;
import com.spring.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 自定义认证实现
 * Created by chenxizhong on 2018/5/5.
 */
public class AuthProvider  implements AuthenticationProvider {

    @Autowired
    private IUserService userService;

    private Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String inputPassWord = (String) authentication.getCredentials();
        User user = userService.findUserByName(userName);
        if(user == null){
            throw new AuthenticationCredentialsNotFoundException("authError");
        }
        // 通过 md5 加密 使用用户id作为加颜
        if(this.passwordEncoder.isPasswordValid(user.getPassword(),inputPassWord,user.getId())){
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }

        throw new BadCredentialsException("authError");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
