package com.interceptly.api.dao.composites;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
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
