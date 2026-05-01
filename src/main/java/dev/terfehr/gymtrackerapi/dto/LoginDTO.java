package dev.terfehr.gymtrackerapi.dto;

import dev.terfehr.gymtrackerapi.model.User;

public record LoginDTO(UserDTO userDto, String refreshToken, String accessToken) {
    public LoginDTO(User user, String refreshToken, String accessToken) {
        this(new UserDTO(user), refreshToken, accessToken);
    }
}
