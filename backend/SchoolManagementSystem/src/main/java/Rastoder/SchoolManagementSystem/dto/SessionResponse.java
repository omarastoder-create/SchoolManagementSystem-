package Rastoder.SchoolManagementSystem.dto;

import Rastoder.SchoolManagementSystem.model.StudentGroup;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessionResponse(
        UUID sessionId,
        LocalDateTime startDate,
        String content,
        UUID studentGroup
) {
}
