package org.example.queuemehospital.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "name is required")
    @Size(max = 30, message = "name max length 30")
    @Column(columnDefinition = "varchar(30) not null")
    private String name;

    @NotEmpty(message = "username is required")
    @Size(max = 30, message = "username max length 30")
    @Column(columnDefinition = "varchar(30) unique not null")
    private String username;

    @NotEmpty(message = "specialization is required")
    @Size(max = 30, message = "specialization max length 30")
    @Column(columnDefinition = "varchar(30) not null")
    private String specialization;

    @NotNull(message = "isLeave flag is required")
    @Column(columnDefinition = "boolean default false")
    private Boolean isLeave = false;

    @NotNull(message = "departmentId is required")
    @Positive(message = "departmentId must be positive")
    @Column(columnDefinition = "INT")
    private Integer departmentId;

    @NotNull(message = "hospitalId is required")
    @Positive(message = "hospitalId must be positive")
    @Column(columnDefinition = "INT")
    private Integer hospitalId;

}
