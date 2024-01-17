package demo.security.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Entity @Data
@Table(name="person")
public class Person {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Name mustn't be empty")
    @Size(min = 5, max = 20, message = "Name must be from 5 to 20 chars")
    @Column(name = "username")
    private String username;

    @Min(value = 1900, message = "Minimum year of birth 1900")
    @Column(name = "birthyear")
    private int birthYear;

    @NotEmpty(message = "Password mustn't be empty")
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role = "ROLE_USER";

    @Column(name = "color")
    private String color = "#808080";

}
