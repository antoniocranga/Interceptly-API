package com.interceptly.api.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Formula;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comments")
public class CommentDao extends BaseEntity {

    @Column(name = "user_id", updatable = false, columnDefinition = "INT UNSIGNED")
    private Integer userId;

    @Formula("(select u.email from users u where u.id = user_id)")
    private String email;

    @JsonIgnore
    @Column(name = "issue_id", updatable = false, columnDefinition = "INT UNSIGNED")
    private Integer issueId;

    @Column(name = "comment", columnDefinition = "TEXT")
    @Lob
    private String comment;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "issue_id", insertable = false, updatable = false)
    private IssueDao issue;
}
