package net.ketlas.studentspringmvc.sec.services;

import groovy.util.logging.Slf4j;
import lombok.AllArgsConstructor;

import net.ketlas.studentspringmvc.sec.entities.AppRole;
import net.ketlas.studentspringmvc.sec.entities.AppUser;
import net.ketlas.studentspringmvc.sec.repositories.AppRoleRepository;
import net.ketlas.studentspringmvc.sec.repositories.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@AllArgsConstructor
@Service
@Transactional
//For logs
@Slf4j
public class SecurityServiceImpl implements SecurityService {

    private AppRoleRepository appRoleRepository;
    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;


    @Override
    public AppUser saveNewUser(String username, String password, String rePassword) {

        if (!password.equals(rePassword))
            throw new RuntimeException("Passwords not match!");

        AppUser appUser = AppUser.builder()
                .userId(UUID.randomUUID().toString())
                .username(username)
                .password(passwordEncoder.encode(password))
                .active(true).build();
        return appUserRepository.save(appUser);
    }

    @Override
    public AppRole saveNewRole(String roleName, String roleDescription) {

        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        if (appRole != null)
            throw new RuntimeException("Role "+roleName+ " already exist!");
        return appRoleRepository
                .save(AppRole.builder()
                        .roleName(roleName)
                        .description(roleDescription)
                        .build());
    }

    @Override
    public void addRoleToUser(String username, String roleName) {

        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findByRoleName(roleName);

        if (appUser == null)
            throw new RuntimeException("User Not Found");
        if (appRole == null)
            throw new RuntimeException("Role Not Found");

        appUser.getRoles().add(appRole);
        //commit will be auto...
        //no need to add
        //appUserRepository.add(appUser);
        //bcz the class is Transactional
    }

    @Override
    public void removeRoleFromUser(String username, String roleName) {

        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        if (appUser == null)
            throw new RuntimeException("User Not Found");
        if (appRole == null)
            throw new RuntimeException("Role Not Found");
        appUser.getRoles().remove(appRole);
    }

    @Override
    public AppUser loadUserByUserName(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public AppRole loadRoleByRoleName(String roleName) {
        return appRoleRepository.findByRoleName(roleName);
    }
}
