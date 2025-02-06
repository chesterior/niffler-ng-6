package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.UUID;

public record UserJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("firstname")
        String firstname,
        @JsonProperty("surname")
        String surname,
        @JsonProperty("photo")
        byte[] photo,
        @JsonProperty("photo_small")
        byte[] photoSmall,
        @JsonProperty("full_name")
        String fullName,
        @JsonProperty("FriendshipStatus")
        FriendshipStatus friendshipStatus) {

    public static UserJson fromEntity(UserEntity entity, FriendshipStatus friendshipStatus) {
        return new UserJson(
                entity.getId(),
                entity.getUsername(),
                entity.getCurrency(),
                entity.getFirstname(),
                entity.getSurname(),
                entity.getPhoto(),
                entity.getPhotoSmall(),
                entity.getFullname(),
                friendshipStatus
        );
    }
}
