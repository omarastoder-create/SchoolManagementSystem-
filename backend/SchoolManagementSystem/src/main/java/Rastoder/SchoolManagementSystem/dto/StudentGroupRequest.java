package Rastoder.SchoolManagementSystem.dto;

import Rastoder.SchoolManagementSystem.model.Room;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;
import java.util.UUID;

public record StudentGroupRequest(
        Room room,
        UUID teacherId,
        Set<UUID> studentsIds

) {
}
