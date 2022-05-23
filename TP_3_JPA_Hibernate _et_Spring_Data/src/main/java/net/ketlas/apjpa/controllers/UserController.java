package net.ketlas.apjpa.controllers;

import net.ketlas.apjpa.entities.User;
import net.ketlas.apjpa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping("/users/{username}")
    User User(@PathVariable("username") String username){
        User user = userService.findUserByUserName(username);
        return user;
    }
}
