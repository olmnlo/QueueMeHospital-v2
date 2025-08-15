package org.example.queuemehospital.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Scheduler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "available days are required")
    @Size(min = 1, message = "must contain at least 1 day")
    @Column(columnDefinition = "TEXT NOT NULL")
    @Convert(converter = StringListConverter.class)
    private List<@Pattern(regexp = "^(sunday|monday|tuesday|wednesday|thursday|friday|saturday)$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "invalid day") String> availableDays;

    @NotNull(message = "doctor id is required")
    @Positive(message = "doctor id must be positive")
    @Column(columnDefinition = "INT")
    private Integer doctorId;

}

@Converter
class StringListConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null) return null;
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting list to JSON", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<String>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON from DB", e);
        }
    }
}

