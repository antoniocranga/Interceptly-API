package com.interceptly.api.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interceptly.api.dao.composites.UserProjectComposite;
import com.interceptly.api.util.enums.PermissionEnum;
import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "permissions")
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
}
