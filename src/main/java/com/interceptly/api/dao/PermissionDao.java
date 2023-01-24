package com.interceptly.api.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interceptly.api.dao.composites.UserProjectComposite;
import com.interceptly.api.util.enums.PermissionEnum;
import lombok.*;
import org.apache.catalina.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id", nullable = false, insertable = false, updatable = false)
    private ProjectDao project;

    @Column(name = "permission", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private PermissionEnum permission;
}
