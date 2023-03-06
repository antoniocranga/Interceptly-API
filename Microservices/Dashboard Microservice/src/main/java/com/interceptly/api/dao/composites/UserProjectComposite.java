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
public class UserProjectComposite implements Serializable {
    @Column(name = "user_id", columnDefinition = "INT UNSIGNED")
    private Integer userId;
    @Column(name = "project_id", columnDefinition = "INT UNSIGNED")
    private Integer projectId;
}
