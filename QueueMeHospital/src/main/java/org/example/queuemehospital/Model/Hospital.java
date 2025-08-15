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
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "name is required")
    @Size(max = 255, message = "name max length 255")
    @Column(columnDefinition = "varchar(255) not null")
    private String name;

    @NotNull(message = "latitude is required")
    @DecimalMin(value = "-90.0", inclusive = true, message = "latitude must be >= -90")
    @DecimalMax(value = "90.0", inclusive = true, message = "latitude must be <= 90")
    private Double latitude;

    @NotNull(message = "longitude is required")
    @DecimalMin(value = "-180.0", inclusive = true, message = "longitude must be >= -180")
    @DecimalMax(value = "180.0", inclusive = true, message = "longitude must be <= 180")
    private Double longitude;
}
