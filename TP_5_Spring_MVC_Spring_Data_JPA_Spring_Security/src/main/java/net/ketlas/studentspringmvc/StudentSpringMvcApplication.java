package net.ketlas.studentspringmvc;

import net.ketlas.studentspringmvc.entities.Etudiant;
import net.ketlas.studentspringmvc.entities.Gener;
import net.ketlas.studentspringmvc.repositories.EtudiantRepo;
import net.ketlas.studentspringmvc.sec.entities.AppUser;
import net.ketlas.studentspringmvc.sec.services.SecurityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.UUID;

@SpringBootApplication
public class StudentSpringMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentSpringMvcApplication.class, args);
    }

    @Bean
    CommandLineRunner start(EtudiantRepo etudiantRepo){
        return args -> {

//            for (int i = 0; i < 100; i++) {
//                Etudiant etudiant1 = Etudiant.builder()
//                        .nom("nom "+i).prenom("prenom "+i).email("email"+i+"@a.com")
//                        .dateNaissance(new Date()).gener(i%2==0?Gener.MASCULIN:Gener.FEMININ).build();
//                etudiantRepo.save(etudiant1);
//            }

        };
    }

    //@Bean
    CommandLineRunner saveUsers(SecurityService securityService){
        return args -> {

            securityService.saveNewUser("hassan","hassan","hassan");
            securityService.saveNewUser("admin","admin","admin");

            securityService.saveNewRole("ADMIN","");
            securityService.saveNewRole("USER","");

            securityService.addRoleToUser("hassan","USER");
            securityService.addRoleToUser("admin","ADMIN");
            securityService.addRoleToUser("admin","USER");

        };
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
