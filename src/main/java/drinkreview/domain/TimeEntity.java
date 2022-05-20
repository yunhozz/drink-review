package drinkreview.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
public class TimeEntity {

    @CreatedDate
    @Column(updatable = false, columnDefinition = "datetime(6)")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(columnDefinition = "datetime(6)")
    private LocalDateTime lastModifiedDate;
}
