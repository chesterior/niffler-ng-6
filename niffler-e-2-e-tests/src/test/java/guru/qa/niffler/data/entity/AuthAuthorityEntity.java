package guru.qa.niffler.data.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class AuthAuthorityEntity implements Serializable {

    private UUID id;
    private AuthUserEntity user;
    private Authority authority;
}
