package Rastoder.SchoolManagementSystem.dto;

import Rastoder.SchoolManagementSystem.model.Language;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record StudentResponse(
        UUID studentId,
        String firstName,
        String lastName,
        LocalDate birthDate,
        int level,
        Language language,
        Set<UUID> parentIds

) {
}
