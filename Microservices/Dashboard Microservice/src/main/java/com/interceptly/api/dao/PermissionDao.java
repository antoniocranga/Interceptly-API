package com.interceptly.api.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interceptly.api.dao.composites.UserProjectComposite;
import com.interceptly.api.util.enums.PermissionEnum;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "permissions")
@EntityListeners(AuditingEntityListener.class)
public class PermissionDao implements Serializable {

    @EmbeddedId
    @JsonIgnore
    UserProjectComposite id;

    //    @ManyToOne
//    @MapsId("userId")
//    @JoinColumn(name = "user_id")
//    @JsonIgnore
//    private UserDao user;
    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    @JsonIgnore
    private Integer userId;

    @Formula("(select u.email from users u where u.id = user_id)")
    @JsonIgnore
    private String email;

    @Column(name = "project_id", nullable = false, insertable = false, updatable = false)
    @JsonIgnore
    private Integer projectId;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id", nullable = false, insertable = false, updatable = false)
    private ProjectDao project;

    @Column(name = "permission", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private PermissionEnum permission;

    @CreatedDate
    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @NotNull
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", nullable = false)
    @JsonIgnore
    private Integer createdBy;
}
