package Rastoder.SchoolManagementSystem.dto;

import Rastoder.SchoolManagementSystem.model.Language;

import java.util.Set;
import java.util.UUID;

public record TeacherResponse(
        UUID teacherId,
        String firstName,
        String familyName,
        String phoneNumber,
        String description,
        Set<Language> languages,
        boolean isActive
) {
}
