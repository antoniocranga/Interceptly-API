package com.interceptly.api.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interceptly.api.util.ProviderEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class UserDao extends BaseEntity {

    @Column(name = "first_name", columnDefinition = "VARCHAR(20)")
    @Size(max = 20)
    private String firstName;

    @Column(name = "last_name", columnDefinition = "VARCHAR(20)")
    @Size(max = 20)
    private String lastName;

    @Column(name = "username", columnDefinition = "VARCHAR(100)")
    @Size(max = 100)
    private String username;

    @Column(name = "email", unique = true, columnDefinition = "VARCHAR(100)")
    @Size(max = 100)
    @Email
    private String email;

    @Column(name = "verified", columnDefinition = "TINYINT(1) DEFAULT 0")
    @ColumnDefault(value = "0")
    private Integer verified;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "password", columnDefinition = "VARCHAR(100)")
    @Size(max = 100)
    @JsonIgnore
    private String password;

    @Column(name = "provider", columnDefinition = "INT")
    @Enumerated(EnumType.ORDINAL)
    private ProviderEnum provider;

//    @OneToMany(mappedBy = "user")
//    @JsonManagedReference(value = "user-permissions")
//    private List<PermissionDao> permissions;
}
