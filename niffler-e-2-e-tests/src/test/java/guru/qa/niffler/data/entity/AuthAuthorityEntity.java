package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.AuthAuthorityJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class AuthAuthorityEntity implements Serializable {

    private UUID id;
    private AuthUserEntity user;
    private String authority;

    public static AuthAuthorityEntity fromJson(AuthAuthorityJson json, AuthUserEntity userEntity) {
        AuthAuthorityEntity aa = new AuthAuthorityEntity();
        aa.setId(json.id());
        aa.setUser(userEntity);
        aa.setAuthority(json.authority());
        return aa;
    }
}
