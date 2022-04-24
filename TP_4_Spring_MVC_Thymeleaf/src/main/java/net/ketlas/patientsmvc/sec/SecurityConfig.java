package net.ketlas.patientsmvc.sec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        PasswordEncoder passwordEncoder = passwordEncoder();
        String encodedPasswordUser = passwordEncoder.encode("hassan");
        String encodedPasswordAdmin = passwordEncoder.encode("admin");
//        1.In Memory Auth
//        auth.inMemoryAuthentication()
//                .withUser("hassan")
//                .password(encodedPasswordUser)
//                .roles("USER")
//                .and()
//                .withUser("admin")
//                .password(encodedPasswordAdmin)
//                .roles("ADMIN","USER");

//        2.JDBC Auth

//        3.User Details Service

        auth.userDetailsService(userDetailsService);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin();
        http.authorizeRequests().antMatchers("/delete/**").hasRole("ADMIN");
        http.authorizeRequests().antMatchers("/webjars/**").permitAll();
//        http.authorizeRequests().antMatchers("/user/**").hasRole("USER");
//        http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN");
//        http.authorizeRequests().anyRequest().authenticated();///!!important
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
