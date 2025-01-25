package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.AuthUserEntity;

import java.util.UUID;

public record AuthUserJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("password")
        String password,
        @JsonProperty("enabled")
        boolean enabled,
        @JsonProperty("account_non_expired")
        boolean accountNonExpired,
        @JsonProperty("account_non_locked")
        boolean accountNonLocked,
        @JsonProperty("credentials_non_expired")
        boolean credentialsNonExpired) {

    public static AuthUserJson fromEntity(AuthUserEntity entity) {
        return new AuthUserJson(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.isEnable(),
                entity.isAccountNonExpired(),
                entity.isAccountNonLocked(),
                entity.isCredentialsNonExpired()
        );
    }
}
