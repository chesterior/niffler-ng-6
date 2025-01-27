package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.AuthUserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class AuthUserEntity implements Serializable {

    private UUID id;
    private String username;
    private String password;
    private boolean enable;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

    public static AuthUserEntity fromJson(AuthUserJson json) {
        AuthUserEntity au = new AuthUserEntity();
        au.setId(json.id());
        au.setUsername(json.username());
        au.setPassword(json.password());
        au.setEnable(json.enabled());
        au.setAccountNonExpired(json.accountNonExpired());
        au.setAccountNonLocked(json.accountNonLocked());
        au.setCredentialsNonExpired(json.credentialsNonExpired());
        return au;
    }
}
