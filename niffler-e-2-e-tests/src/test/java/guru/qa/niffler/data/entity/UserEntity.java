package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class UserEntity implements Serializable {

    private UUID id;
    private String username;
    private CurrencyValues currency;
    private String firstname;
    private String surname;
    private byte[] photo;
    private byte[] photoSmall;
    private String fullName;

    public static UserEntity fromJson(UserJson json) {
        UserEntity us = new UserEntity();
        us.setId(json.id());
        us.setUsername(json.username());
        us.setCurrency(json.currency());
        us.setFirstname(json.firstname());
        us.setSurname(json.surname());
        us.setPhoto(json.photo());
        us.setPhotoSmall(json.photoSmall());
        us.setFullName(json.fullName());
        return us;
    }
}
