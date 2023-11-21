package org.ericwubbo.demo.user;

import java.util.UUID;

public record UserDto(UUID id, String username) {
    public UserDto(User user) {
        this(user.getId(), user.getUsername());
    }
}
