package dev.terfehr.gymtrackerapi.dto;

import dev.terfehr.gymtrackerapi.model.User;

public record UserDTO(Long id, String firstName, String lastName, String username, String email, String reservedEmail, String role, boolean enabled) {
    public UserDTO(User user) {
        this(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getReservedEmail(), user.getRole(), user.isEnabled());
    }
}
