package org.maksim.training.mtapp.config;

import org.maksim.training.mtapp.model.UserRole;
import org.maksim.training.mtapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final DataSource dataSource;

    @Autowired
    public SecurityConfig(UserService userService, DataSource dataSource) {
        this.userService = userService;
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                        .antMatchers("/resources/**").permitAll()
                        .antMatchers("/util/**").permitAll()
                        .antMatchers("/favicon.ico").permitAll()
                        .antMatchers("/error**").permitAll()
                        .antMatchers("/users/upload**").permitAll()
                        .antMatchers("/events/upload**").permitAll()
                        .antMatchers("/rest/**").permitAll()
                        .antMatchers("/rest**").permitAll()
                        .antMatchers("/tickets/**").hasRole(UserRole.BOOKING_MANAGER.getAuthorityName())
                        .anyRequest().hasRole(UserRole.REGISTERED_USER.getAuthorityName())
                .and()

                .formLogin()
                        .loginPage("/login")
                        .loginProcessingUrl("/j_spring_security_check")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .permitAll()
                .and()

                .logout()
                        .logoutUrl("/j_spring_security_logout")
                        .logoutSuccessUrl("/login?logout")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .permitAll()
                .and()

                .csrf().disable()

                .rememberMe()
                        .tokenValiditySeconds(15 * 60)
                        .userDetailsService(userService)
                        .tokenRepository(persistentTokenRepository())
                        .rememberMeCookieName("remember_me_cookie");
    }
}