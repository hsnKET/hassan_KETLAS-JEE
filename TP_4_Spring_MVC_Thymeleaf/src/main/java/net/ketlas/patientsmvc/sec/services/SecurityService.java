package net.ketlas.patientsmvc.sec.services;

import net.ketlas.patientsmvc.sec.entities.AppRole;
import net.ketlas.patientsmvc.sec.entities.AppUser;

public interface SecurityService {
    AppUser saveNewUser(String username,String password,String rePassword);
    AppRole saveNewRole(String roleName,String roleDescription);
    void addRoleToUser(String username,String roleName);
    void removeRoleFromUser(String username,String roleName);
    AppUser loadUserByUserName(String username);
    AppRole loadRoleByRoleName(String roleName);
}
