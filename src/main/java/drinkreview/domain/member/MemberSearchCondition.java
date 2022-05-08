package drinkreview.domain.member;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberSearchCondition {

    private String memberId;
    private String name;
    private Integer age;
    private Integer ageGoe;
    private Integer ageLoe;
    private String auth;
}
