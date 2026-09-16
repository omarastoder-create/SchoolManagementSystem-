package Rastoder.SchoolManagementSystem.dto;

import Rastoder.SchoolManagementSystem.model.Room;

import java.util.Set;
import java.util.UUID;

public record StudentGroupResponse(
        UUID groupId,
        Room room,
        Set<UUID> session,
        UUID teacherId,
        Set<UUID> studentsIds
) {
}
