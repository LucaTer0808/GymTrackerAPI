package dev.terfehr.gymtrackerapi.dto;

import dev.terfehr.gymtrackerapi.model.User;

public record UserDTO(Long id, String firstName, String lastName, String email, String reservedEmail) {
    public UserDTO(User user) {
        this(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getReservedEmail());
    }
}
