package com.spring.config;

import com.spring.security.LoginAuthFailHandler;
import com.spring.security.AuthProvider;
import com.spring.security.LoginUrlEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * web security 配置文件
 * Created by chenxizhong on 2018/5/5.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * HTTP 权限管理
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 资源访问权限
        http.authorizeRequests()
                .antMatchers("/admin/login").permitAll() // 管理员登陆入口
                .antMatchers("/static/**").permitAll() // 管理员访问的静态资源
                .antMatchers("/user/login").permitAll() // 普通用户登陆入口
                .antMatchers("/admin/**").hasRole("ADMIN") // 接口访问权限
                .antMatchers("/user/**").hasAnyRole("ADMIN","USER")
                .antMatchers("/api/user/**").hasAnyRole("ADMIN","USER")
                .and()
                .formLogin()
                .loginProcessingUrl("/login") // 配置角色登陆处理入口
                .failureHandler(authFailHandler()) // 错误跳转页面
                .and()
                .logout()
                .logoutUrl("/logout")
                // 用户退出成功跳转指定页面
                .logoutSuccessUrl("/logout/page")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                // 配置用户权限跳转制定页面
                .exceptionHandling()
                .authenticationEntryPoint(urlEntryPoint())
                .accessDeniedPage("/403");

        // 关闭防御策略 为了方便开发
        http.csrf().disable();
        // 后台使用 H-UI-Admin  使用 iframe 开发 需要配置同源策略
        http.headers().frameOptions().sameOrigin();
    }

    /**
     * 自定义认证策略
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider()).eraseCredentials(true);
    }

    @Bean
    public AuthProvider authProvider(){
        return new AuthProvider();
    }

    /**
     * 根据用户登陆角色跳转制定页面 默认用户页面
     * @return
     */
    @Bean
    public LoginUrlEntryPoint urlEntryPoint(){
        return new LoginUrlEntryPoint("/user/login");
    }

    @Bean
    public LoginAuthFailHandler authFailHandler(){
        return new LoginAuthFailHandler(urlEntryPoint());
    }
}
