package org.example.queuemehospital.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "name is required")
    @Size(max = 30, message = "name max length 30")
    @Column(columnDefinition = "varchar(30) not null")
    private String name;

    @NotEmpty(message = "username is required")
    @Size(max = 20, message = "username max length 20")
    @Column(columnDefinition = "varchar(20) unique not null")
    private String username;

    @NotEmpty(message = "password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?`~])(?=.{8,}).*$", message = "password must has 1 lower/upper 1 digit 1 special character")
    @Size(max = 20, message = "password max length 20")
    @Column(columnDefinition = "varchar(20) not null")
    private String password;

    @NotEmpty(message = "email is required")
    @Size(max = 50, message = "email max length 50")
    @Email(message = "email is not valid")
    @Column(columnDefinition = "varchar(50) unique not null")
    private String email;

    @NotNull(message = "age is required")
    @Positive(message = "age must be positive")
    @Min(value = 16, message = "your age must be more than 16")
    private Integer age;

    @NotEmpty(message = "blood type is required")
    @Size(max = 3, message = "blood type max length 3")
    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "blood type eather A or B or AB or O +/-")
    @Column(columnDefinition = "varchar(3) not null")
    private String bloodType;


    @NotNull(message = "latitude is required")
    @DecimalMin(value = "-90.0", message = "latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "latitude must be <= 90")
    private Double latitude;

    @NotNull(message = "longitude is required")
    @DecimalMin(value = "-180.0", message = "longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "longitude must be <= 180")
    private Double longitude;


    @Size(max = 20, message = "role max length 20")
    @Pattern(regexp = "^(user)$", message = "role must be eather user or admin")
    @Column(columnDefinition = "varchar(20) not null")
    private String role = "user";

}
