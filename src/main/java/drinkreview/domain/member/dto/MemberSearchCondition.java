package drinkreview.domain.member.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberSearchCondition {

    private String memberId;
    private String name;
    private int ageGoe;
    private int ageLoe;
    private String auth;
}
