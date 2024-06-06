package demo.security.entities;

import demo.security.entities.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;


@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private int id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role = "ROLE_USER";

    @Column(nullable = false)
    private String color = "#808080";


    public User() {}

    public User(UserDTO dto) {
        username = dto.getUsername();
        password = dto.getPassword();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
