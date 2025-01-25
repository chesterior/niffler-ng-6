package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.AuthUserEntity;

import java.util.UUID;

public record AuthAuthorityJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("user_id")
        AuthUserJson userId,
        @JsonProperty("authority")
        String authority) {

    public static AuthAuthorityJson fromEntity(AuthAuthorityEntity entity, AuthUserEntity userEntity) {
        return new AuthAuthorityJson(
                entity.getId(),
                AuthUserJson.fromEntity(userEntity),
                entity.getAuthority()
        );
    }
}
