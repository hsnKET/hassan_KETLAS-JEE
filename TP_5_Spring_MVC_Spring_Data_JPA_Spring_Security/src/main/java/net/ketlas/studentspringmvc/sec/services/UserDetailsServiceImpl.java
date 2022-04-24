package net.ketlas.studentspringmvc.sec.services;

import lombok.AllArgsConstructor;
import net.ketlas.studentspringmvc.sec.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private SecurityService securityService;
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        AppUser appUser = securityService.loadUserByUserName(username);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        appUser.getRoles().forEach( appRole -> {
            //ROLE_ add this prefix
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(appRole.getRoleName());
            authorities.add(simpleGrantedAuthority);
        });
        User springUser = new User(appUser.getUsername(), appUser.getPassword() ,authorities);
        return springUser;
    }
}
