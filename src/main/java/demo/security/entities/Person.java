package demo.security.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;


@Entity @Data
@Table(name="person")
public class Person {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 5, max = 100, message = "Имя должно быть от 5 до 100 символов")
    @Column(name = "username")
    private String username;

    @Min(value = 1900, message = "Минимальный год рождения 1900")
    @Column(name = "birthyear")
    private int birthyear;

    @NotEmpty(message = "Пароль не должен быть пустым")
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role = "ROLE_USER";

}
