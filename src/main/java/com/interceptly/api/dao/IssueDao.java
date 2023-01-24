package com.interceptly.api.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interceptly.api.util.enums.IssueStatusEnum;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Formula;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "issues")
public class IssueDao extends BaseEntity{

    @NonNull
    @Size(max = 65535)
    @Column(name = "title", nullable = false, columnDefinition = "TEXT",length = 65535)
    private String title;

    @NonNull
    @Size(max = 16777215)
    @Column(name="description", nullable = false, columnDefinition = "MEDIUMTEXT",length = 16777215)
    private String description;

    @NonNull
    @Column(name = "status", nullable = false, columnDefinition = "VARCHAR(15) DEFAULT 'ACTIVE'",length = 15)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("ACTIVE")
    private IssueStatusEnum status;

    @NonNull
    @Size(max = 65535)
    @Column(name = "type", nullable = false, columnDefinition = "TEXT", length = 65535)
    private String type;
    @NonNull
    @Size(max = 65535)
    @Column(name = "message", nullable = false, columnDefinition = "MEDIUMTEXT", length = 65535)
    private String message;

    @NonNull
    @Column(name = "project_id", nullable = false, columnDefinition = "INT UNSIGNED")
    @JsonIgnore
    private Integer projectId;

    @NonNull
    @Column(name = "is_bookmarked", nullable = false)
    private Boolean isBookmarked;

    @Formula("(select count(e.issue_id) from events e where e.issue_id = id)")
    private Integer eventsCount;

    @Formula("(select max(e.created_at) from events e where e.issue_id = id)")
    private LocalDateTime lastSeen;

    @Formula("(select min(e.created_at) from events e where e.issue_id = id)")
    private LocalDateTime firstSeen;

    @Transient
    private List<EventDao> events;
}
