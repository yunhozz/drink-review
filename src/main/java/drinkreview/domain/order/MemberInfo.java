package drinkreview.domain.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfo {

    private Long userId;
    private String memberId;
    private String name;

    public MemberInfo(Long userId, String memberId, String name) {
        this.userId = userId;
        this.memberId = memberId;
        this.name = name;
    }
}
