package com.interceptly.api.dao;

import com.interceptly.api.util.ProviderEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


import javax.persistence.*;
import javax.validation.constraints.Email;
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class UserDao extends BaseEntity{

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "email", unique = true)
    @Email
    private String email;

    @Column(name = "verified")
    private Integer verified;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "provider")
    @Enumerated(EnumType.ORDINAL)
    private ProviderEnum provider;

//    @OneToMany(mappedBy = "user")
//    @JsonManagedReference(value = "user-permissions")
//    private List<PermissionDao> permissions;
}
