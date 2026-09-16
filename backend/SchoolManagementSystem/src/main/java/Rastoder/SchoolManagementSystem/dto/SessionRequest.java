package Rastoder.SchoolManagementSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SessionRequest(
        @NotBlank
        String content,

        @NotNull
        UUID studentGroup
) {
}
