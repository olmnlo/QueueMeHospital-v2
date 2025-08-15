package org.example.queuemehospital.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "appointmentDate is required")
    @Column(columnDefinition = "timestamp not null")
    private LocalDateTime appointmentDate;

    @NotNull(message = "appointmentDay is required")
    @Column(columnDefinition = "varchar(20) not null")
    private LocalDateTime appointmentDay;

    @Column(columnDefinition = "text")
    private String description;

    @NotEmpty(message = "status is required")
    @Size(max = 20, message = "status max length 20")
    @Column(columnDefinition = "varchar(20) not null")
    private String status;

    @NotNull(message = "userId is required")
    @Positive(message = "userId must be positive")
    @Column(columnDefinition = "int not null")
    private Integer userId;

    @NotNull(message = "doctorId is required")
    @Positive(message = "doctorId must be positive")
    @Column(columnDefinition = "int not null")
    private Integer doctorId;
}
