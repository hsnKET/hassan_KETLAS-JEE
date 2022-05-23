package net.ketlas.apjpa;

import net.ketlas.apjpa.entities.*;
import net.ketlas.apjpa.repos.ConsultationRepository;
import net.ketlas.apjpa.repos.MedecinRepository;
import net.ketlas.apjpa.repos.PatientRepository;
import net.ketlas.apjpa.repos.RendezVousRepository;
import net.ketlas.apjpa.services.IHospitalService;
import net.ketlas.apjpa.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class ApJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApJpaApplication.class, args);
    }

    @Bean
    CommandLineRunner start(UserService userService){

        return args -> {

            //create user 1
            User  user1 = new User();
            user1.setUsername("hassan");
            user1.setPassword("123455");
            userService.addNewUser(user1);

            //create user 2
            User  user2 = new User();
            user1.setUsername("admin");
            user1.setPassword("admin");
            userService.addNewUser(user1);

            Stream.of("STUDENT","USER","ADMIN")
                    .forEach(roleName->{
                        Role role = new Role();
                        role.setRoleName(roleName);
                        userService.addNewRole(role);
                    });


            // add roles to user
            userService.AddRoleToUser("hassan","STUDENT");
            userService.AddRoleToUser("hassan","USER");
            // add role to admin
            userService.AddRoleToUser("admin","USER");
            userService.AddRoleToUser("admin","ADMIN");

            try{
                User userAuth = userService.authenticate("hassan","123455");
                List<Role> userRole = userAuth.getRoles();
                System.out.println(userAuth);

            }catch (Exception e){
                e.printStackTrace();
            }

        };
    }

    @Bean
    CommandLineRunner start(IHospitalService hospitalService,
                            PatientRepository patientRepository,
                            RendezVousRepository rendezVousRepository,
                            ConsultationRepository consultationRepository,
                            MedecinRepository medecinRepository){
        return args -> {
            Stream.of("moahemmed","ahmed")
                    .forEach(name ->{
                        Patient patient = new Patient();
                        patient.setNom(name);
                        patient.setDateNaissance(new Date());
                        patient.setMalade(false);
                        hospitalService.savePatient(patient);
                    });
            Stream.of("khadija","badr","siham")
                    .forEach(name ->{
                        Medecin medecin = new Medecin();
                        medecin.setNom(name);
                        medecin.setEmail(name+"@gmail.com");
                        medecin.setSpecialite(Math.random()>0.5?"Cardio":"chirurgie");
                        hospitalService.saveMedecin(medecin);
                    });

            Patient patient = patientRepository.findById(1L).orElse(null);
            Patient patient1 = patientRepository.findByNom("Mohammed");

            Medecin medecin = medecinRepository.findByNom("khadija");

            RendezVous rendezVous = new RendezVous();
            rendezVous.setDateCreation(new Date());
            rendezVous.setStatus(StatusRDV.CANCELED);
            rendezVous.setPatient(patient);
            rendezVous.setMedecin(medecin);
            RendezVous savedRDV = hospitalService.saveRendezVous(rendezVous);
            System.out.println(savedRDV.getId());

            RendezVous rendezVous1 = rendezVousRepository.findAll().get(0);
            Consultation consultation = new Consultation();
            consultation.setDateConsultation(new Date());
            consultation.setRendezVous(rendezVous1);
            consultation.setRapportConsultation("rapport...");
            hospitalService.saveConsultation(consultation);
        };
    }
}
