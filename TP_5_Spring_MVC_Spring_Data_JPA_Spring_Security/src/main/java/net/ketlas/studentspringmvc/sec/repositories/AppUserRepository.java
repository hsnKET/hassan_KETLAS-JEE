package net.ketlas.studentspringmvc.sec.repositories;

import net.ketlas.studentspringmvc.sec.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser,String> {
    AppUser findByUsername(String username);
}
