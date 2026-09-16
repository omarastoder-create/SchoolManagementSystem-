package Rastoder.SchoolManagementSystem.dto;

import Rastoder.SchoolManagementSystem.model.Language;

import java.util.Set;

public record TeacherRequest(
        String firstName,
        String familyName,
        String phoneNumber,
        String description,
        Set<Language> languages
) {
}
