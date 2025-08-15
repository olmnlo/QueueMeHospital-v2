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
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "name is required")
    @Size(max = 30, message = "name max length 30")
    @Column(columnDefinition = "varchar(30) not null")
    private String name;

    @NotEmpty(message = "description is required")
    @Column(columnDefinition = "TEXT NOT NULL")
    private String description;

    @NotNull(message = "hospitalId is required")
    @Positive(message = "hospitalId must be positive")
    @Column(columnDefinition = "INT")
    private Integer hospitalId;
}
