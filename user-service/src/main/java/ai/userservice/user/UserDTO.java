package ai.userservice.user;

public record UserDTO(
        String username,
        String displayName,
        String password,
        String email,
        Long userId
) {
}
