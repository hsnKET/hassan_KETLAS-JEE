package net.ketlas.apjpa.repos;

import net.ketlas.apjpa.entities.Role;
import net.ketlas.apjpa.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role,Long> {

    Role findByRoleName(String roleName);
}
