package drinkreview.global.enums;

import lombok.Getter;

@Getter
public enum Role {

    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");
    private String value;

    Role(String value) {
        this.value = value;
    }
}
