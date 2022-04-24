package net.ketlas.apjpa.services;

import net.ketlas.apjpa.entities.Role;
import net.ketlas.apjpa.entities.User;

public interface UserService {

    User addNewUser(User user);
    Role addNewRole(Role role);
    User findUserByUserName(String username);
    Role findUserByRoleName(String username);
    void AddRoleToUser(String userName,String roleName);
    User authenticate(String username,String password);
}
