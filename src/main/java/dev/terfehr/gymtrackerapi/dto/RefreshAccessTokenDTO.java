package dev.terfehr.gymtrackerapi.dto;

import dev.terfehr.gymtrackerapi.model.User;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record RefreshAccessTokenDTO(UserDTO userDto, String refreshToken) {
    public RefreshAccessTokenDTO(User user, String refreshToken) {
        this(new UserDTO(user), refreshToken);
    }
}
