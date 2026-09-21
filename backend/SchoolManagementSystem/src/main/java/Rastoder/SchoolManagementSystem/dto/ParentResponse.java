package Rastoder.SchoolManagementSystem.dto;

import java.util.UUID;

public record ParentResponse(
        UUID parentId,
        String name,
        String surname,
        String email,
        String phoneNumber,
        boolean isActive
) {
}
