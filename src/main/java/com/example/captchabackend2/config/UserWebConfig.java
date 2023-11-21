package com.example.captchabackend2.config;

import com.example.captchabackend2.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
public class UserWebConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    CustomUserDetailService customUserDetailService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/generateCaptcha").permitAll() // Allow GET requests to /signup
                .antMatchers("/signup/register/**").permitAll()
                .antMatchers("/addNote").permitAll()
                .antMatchers("/findNote/{note_id}").permitAll()
                .antMatchers("/updateNote/{note_id}").permitAll()
                .antMatchers("/deleteNote/{note_id}").permitAll()
                .antMatchers("/getNotes/{user_id}").permitAll()
                .antMatchers("/getIdbyUsername/{username}").permitAll()
                .antMatchers("/setComplete/{note_id}").permitAll()
                .antMatchers("/login/{username}/{password}").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authProvider());
    }
    public DaoAuthenticationProvider authProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailService);
        return authenticationProvider;
    }

}
