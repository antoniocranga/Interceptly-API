package com.interceptly.api.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interceptly.api.util.enums.PermissionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Table(name="permissions")
public class PermissionDao{

    @Id
    @Column(name="user_id")
    @JsonIgnore
    private Integer userId;

//    @ManyToOne
//    @JoinColumn(name="user_id", nullable = false, insertable = false, updatable = false)
//    @JsonBackReference(value = "user-permissions")
//    private UserDao user;

    @OneToOne
    @JoinColumn(name="project_id", nullable = false, insertable = false, updatable = false)
    private ProjectDao project;

    @Column(name= "permission")
    @Enumerated(EnumType.ORDINAL)
    private PermissionEnum permission;

}
