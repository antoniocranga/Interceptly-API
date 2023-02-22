package com.interceptly.api.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comments")
public class CommentDao extends BaseEntity {

    @JsonIgnore
    @Column(name = "user_id", updatable = false, columnDefinition = "INT UNSIGNED")
    private Integer userId;

    @JsonIgnore
    @Column(name = "issue_id", updatable = false, columnDefinition = "INT UNSIGNED")
    private Integer issueId;

    @Column(name = "comment", columnDefinition = "VARCHAR(300)", length = 300)
    private String comment;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "issue_id", insertable = false, updatable = false)
    private IssueDao issue;
}
