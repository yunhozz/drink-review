package drinkreview.domain.member.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberQueryDto {

    private String memberId;
    private String name;
    private int age;
    private String auth;

    @QueryProjection
    public MemberQueryDto(String memberId, String name, int age, String auth) {
        this.memberId = memberId;
        this.name = name;
        this.age = age;
        this.auth = auth;
    }
}
