package org.example.queuemehospital.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WaitingList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime timeEntered = LocalDateTime.now();

    @Column(columnDefinition = "INT default(0)")
    private Integer numberInQueue = 0;

    @NotNull(message = "appointment id is required")
    @PositiveOrZero
    private Integer appointmentId;

    @NotNull(message = "doctor id is required")
    @PositiveOrZero
    private Integer doctorId;

    @NotNull(message = "user id is required")
    @PositiveOrZero
    private Integer userId;

    @PrePersist
    public void prePersist() {
        if (timeEntered == null) {
            timeEntered = LocalDateTime.now();
        }
    }
}
