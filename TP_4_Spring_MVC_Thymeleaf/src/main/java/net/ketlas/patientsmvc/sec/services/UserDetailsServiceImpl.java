package net.ketlas.patientsmvc.sec.services;

import net.ketlas.patientsmvc.sec.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private SecurityService securityService;

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
        User springUser = new User(appUser.getUsername(), appUser.getPassword(), authorities);
        return springUser;
    }
}
