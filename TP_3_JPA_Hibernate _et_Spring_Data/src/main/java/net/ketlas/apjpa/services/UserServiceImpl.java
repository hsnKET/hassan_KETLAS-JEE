package net.ketlas.apjpa.services;

import net.ketlas.apjpa.entities.Role;
import net.ketlas.apjpa.entities.User;
import net.ketlas.apjpa.repos.RoleRepo;
import net.ketlas.apjpa.repos.UserRepo;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private RoleRepo roleRepo;

    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public User addNewUser(User user) {
        user.setUserId(UUID.randomUUID().toString());

        return userRepo.save(user);
    }

    @Override
    public Role addNewRole(Role role) {
        return roleRepo.save(role);
    }

    @Override
    public User findUserByUserName(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public Role findUserByRoleName(String roleName) {
        return roleRepo.findByRoleName(roleName);
    }

    @Override
    public void AddRoleToUser(String userName, String roleName) {
        User user = findUserByUserName(userName);
        Role role = findUserByRoleName(roleName);
        user.getRoles().add(role);
        role.getUsers().add(user);

    }

    @Override
    public User authenticate(String username, String password) {
        User user = findUserByUserName(username);
        if (user != null &&
            user.getPassword().equals(password))
            return user;
        throw new RuntimeException("Bad credentialag");
    }
}
