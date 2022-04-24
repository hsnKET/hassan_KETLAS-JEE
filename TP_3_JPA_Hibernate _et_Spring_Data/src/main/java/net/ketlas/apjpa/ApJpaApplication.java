package net.ketlas.apjpa;

import net.ketlas.apjpa.entities.Role;
import net.ketlas.apjpa.entities.User;
import net.ketlas.apjpa.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
}
