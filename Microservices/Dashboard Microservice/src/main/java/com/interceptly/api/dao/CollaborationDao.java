package com.interceptly.api.dao;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interceptly.api.dao.composites.UserIssueComposite;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "collaborations")
public class CollaborationDao implements Serializable {
    @EmbeddedId
    @JsonIgnore
    UserIssueComposite id;

    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    private Integer userId;

    @Formula("(select u.email from users u where u.id = user_id)")
    private String email;

    @Formula("(select u.email from users u where u.id = created_by)")
    private String createdByEmail;

    @Column(name = "issue_id", nullable = false, insertable = false, updatable = false)
    @JsonIgnore
    private Integer issueId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "issue_id", insertable = false, updatable = false)
    private IssueDao issue;

    @CreatedDate
    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false, updatable = false, columnDefinition = "INT UNSIGNED")
    @JsonIgnore
    private Integer createdBy;
}
