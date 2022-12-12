package com.interceptly.api.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interceptly.api.util.enums.IssueStatusEnum;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "issues")
public class IssueDao extends BaseEntity{

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private IssueStatusEnum status;

    @Formula("(select count(e.issue_id) from events e where e.issue_id = id)")
    private Integer eventsCount;

    @Transient
    private List<EventDao> events;
//    @OneToMany(mappedBy = "issue")
//    private Set<EventDao> events;
}
