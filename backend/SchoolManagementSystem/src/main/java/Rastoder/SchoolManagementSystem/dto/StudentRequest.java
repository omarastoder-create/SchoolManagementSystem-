package Rastoder.SchoolManagementSystem.dto;

import Rastoder.SchoolManagementSystem.model.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record StudentRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotNull
        LocalDate birthDate,
        @Positive
        int level,
        @NotNull
        Language language,
        Set<UUID> parentIds
) {
}
