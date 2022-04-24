package net.ketlas.patientsmvc;

import net.ketlas.patientsmvc.entities.Patient;
import net.ketlas.patientsmvc.repositories.PatientRepository;
import net.ketlas.patientsmvc.sec.services.SecurityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class PatientsMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatientsMvcApplication.class, args);
    }


    CommandLineRunner start(PatientRepository patientRepository){
        return args -> {

            patientRepository.save(new Patient(null,"hassan",new Date(),false,10));
            patientRepository.save(new Patient(null,"ahmed",new Date(),true,10));
            patientRepository.save(new Patient(null,"jamal",new Date(),false,10));
            patientRepository.save(new Patient(null,"jawad",new Date(),true,12));

        };
    }

    //@Bean
    CommandLineRunner saveUser(SecurityService securityService){
        return args -> {
            securityService.saveNewUser("mohamed","1234","1234");
            securityService.saveNewUser("yasmine","1234","1234");
            securityService.saveNewUser("hassan","1234","1234");

            securityService.saveNewRole("USER","");
            securityService.saveNewRole("ADMIN","");

            securityService.addRoleToUser("mohamed","USER");
            securityService.addRoleToUser("mohamed","ADMIN");
            securityService.addRoleToUser("yasmine","USER");
            securityService.addRoleToUser("hassan","USER");


        };
    }

}
