package com.interceptly.api.dao.composites;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserIssueComposite implements Serializable {
    @Column(name = "user_id", columnDefinition = "INT UNSIGNED")
    private Integer userId;
    @Column(name = "issue_id", columnDefinition = "INT UNSIGNED")
    private Integer issueId;
}
