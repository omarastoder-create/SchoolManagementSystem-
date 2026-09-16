package Rastoder.SchoolManagementSystem.dto;

public record ParentRequest(
        String name,
        String surname,
        String email,
        String phoneNumber
) {
}
