package net.ketlas.apjpa.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    private String userId;
    @Column(unique = true,length = 20,name = "USER_NAME")
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "USERS_ROLES") // default value
    @ToString.Exclude
    private List<Role> roles = new ArrayList<>();
}
